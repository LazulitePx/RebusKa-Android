package com.example.rebuska.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rebuska.data.model.Negocio
import com.example.rebuska.data.repository.NegocioRepository
import kotlinx.coroutines.launch

class NegocioViewModel : ViewModel() {

    fun crearNegocio(negocio: Negocio, logoBytes: ByteArray?, bannerBytes: ByteArray?) {
        viewModelScope.launch {
            NegocioRepository.crearNegocio(negocio)
            //  luego agregar Storage, para aquí subir las imágenes
        }
    }
}
