package com.example.rebuska.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rebuska.data.model.Negocio
import com.example.rebuska.data.repository.NegocioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Cargando : HomeUiState()
    data class Exito(val negocios: List<Negocio>) : HomeUiState()   // Estado exitoso, contiene la lista de negocios obtenidos
    data class Error(val mensaje: String) : HomeUiState()
}

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Cargando)
    val uiState: StateFlow<HomeUiState> = _uiState

    private val _todosLosNegocios = MutableStateFlow<List<Negocio>>(emptyList())
    // Lista completa de negocios obtenidos desde el repositorio
    val busqueda = MutableStateFlow("")


    // Lista filtrada dinámicamente según la búsqueda del usuario
    val negociosFiltrados: StateFlow<List<Negocio>> = combine(_todosLosNegocios, busqueda) { negocios, query ->
        // Si no hay texto de búsqueda, se muestran todos los negocios
        if (query.isBlank()) negocios
        else {
            // Normaliza el texto de búsqueda para ignorar tildes y mayúsculas
            val queryNormalizada = query
                .lowercase()
                .replace("á", "a").replace("é", "e").replace("í", "i")
                .replace("ó", "o").replace("ú", "u").replace("ü", "u")
                .replace("ñ", "n")

            // Filtra negocios por nombre, categoría o descripción
            negocios.filter { negocio ->
                // Función local para normalizar textos
                fun String.normalizar() = this.lowercase()
                    .replace("á", "a").replace("é", "e").replace("í", "i")
                    .replace("ó", "o").replace("ú", "u").replace("ü", "u")
                    .replace("ñ", "n")

                negocio.nombre.normalizar().contains(queryNormalizada) ||
                        negocio.categoria.normalizar().contains(queryNormalizada) ||
                        negocio.descripcion.normalizar().contains(queryNormalizada)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        cargarNegocios()
    }

    fun cargarNegocios() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Cargando
            NegocioRepository.getNegocios()
                .onSuccess { negocios ->
                    _todosLosNegocios.value = negocios
                    _uiState.value = HomeUiState.Exito(negocios)
                }
                .onFailure { error ->
                    _uiState.value = HomeUiState.Error(error.message ?: "Error al cargar negocios")
                }
        }
    }

    private val _totalNoLeidos = MutableStateFlow(0)
    val totalNoLeidos: StateFlow<Int> = _totalNoLeidos

    fun escucharNoLeidos() {
        val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: return

        com.google.firebase.firestore.FirebaseFirestore.getInstance()
            .collection("chats")
            .whereEqualTo("idUsuario1", uid)
            .addSnapshotListener { snapshot, _ ->
                calcularNoLeidos(uid, snapshot)
            }

        com.google.firebase.firestore.FirebaseFirestore.getInstance()
            .collection("chats")
            .whereEqualTo("idUsuario2", uid)
            .addSnapshotListener { snapshot, _ ->
                calcularNoLeidos(uid, snapshot)
            }
    }

    private fun calcularNoLeidos(uid: String, snapshot: com.google.firebase.firestore.QuerySnapshot?) {
        snapshot ?: return
        var total = 0
        snapshot.documents.forEach { doc ->
            val idUsuario1 = doc.getString("idUsuario1") ?: ""
            val campo = if (uid == idUsuario1) "noLeidosUsuario1" else "noLeidosUsuario2"
            total += doc.getLong(campo)?.toInt() ?: 0
        }
        _totalNoLeidos.value = total
    }
}