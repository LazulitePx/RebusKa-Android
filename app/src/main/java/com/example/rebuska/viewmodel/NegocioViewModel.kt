package com.example.rebuska.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rebuska.data.model.Negocio
import com.example.rebuska.data.repository.NegocioRepository // Importa NegocioRepository
import kotlinx.coroutines.launch
import android.util.Log // Importa Log para depuración

class NegocioViewModel : ViewModel() {

    // La función principal para crear un negocio y subir imágenes
    fun crearNegocio(
        nombre: String,
        descripcion: String,
        categoria: String,
        logoBytes: ByteArray?,
        bannerBytes: ByteArray?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            Log.d("NegocioViewModel", "crearNegocio: Iniciando creación de negocio.")
            NegocioRepository.crearNegocio(nombre, descripcion, categoria, logoBytes, bannerBytes)
                .onSuccess {
                    Log.d("NegocioViewModel", "crearNegocio: Negocio creado exitosamente.")
                    onSuccess()
                }
                .onFailure { e ->
                    Log.e("NegocioViewModel", "crearNegocio: Error al crear negocio.", e)
                    onError(e.message ?: "Error desconocido al guardar el negocio")
                }
        }
    }
}