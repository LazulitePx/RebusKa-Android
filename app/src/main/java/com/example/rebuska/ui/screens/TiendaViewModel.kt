package com.example.rebuska.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rebuska.data.model.Negocio
import com.example.rebuska.data.model.Publicacion
import com.example.rebuska.data.repository.NegocioRepository
import com.example.rebuska.data.repository.PublicacionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class TiendaUiState {
    object Cargando : TiendaUiState()
    data class Exito(
        val negocio: Negocio,
        val publicaciones: List<Publicacion>
    ) : TiendaUiState()
    data class Error(val mensaje: String) : TiendaUiState()
}

class TiendaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<TiendaUiState>(TiendaUiState.Cargando)
    val uiState: StateFlow<TiendaUiState> = _uiState

    private var idCargado = ""

    fun cargar(idNegocio: String) {
        if (idNegocio.isBlank()) {
            _uiState.value = TiendaUiState.Error("ID de negocio vacío")
            return
        }
        if (idNegocio == idCargado) return  // evitar recargas innecesarias

        idCargado = idNegocio
        viewModelScope.launch {
            _uiState.value = TiendaUiState.Cargando

            val negocioResult = NegocioRepository.getNegocioById(idNegocio)
            if (negocioResult.isFailure) {
                _uiState.value = TiendaUiState.Error("Negocio no encontrado: $idNegocio")
                return@launch
            }

            val publicacionesResult = PublicacionRepository.getPublicacionesByNegocio(idNegocio)
            if (publicacionesResult.isFailure) {
                _uiState.value = TiendaUiState.Error("Error al cargar publicaciones")
                return@launch
            }

            _uiState.value = TiendaUiState.Exito(
                negocio       = negocioResult.getOrThrow(),
                publicaciones = publicacionesResult.getOrThrow()
            )
        }
    }
}