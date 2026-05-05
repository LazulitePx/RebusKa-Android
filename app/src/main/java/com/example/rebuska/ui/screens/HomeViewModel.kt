package com.example.rebuska.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rebuska.data.model.Negocio
import com.example.rebuska.data.repository.NegocioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Cargando : HomeUiState()
    data class Exito(val negocios: List<Negocio>) : HomeUiState()
    data class Error(val mensaje: String) : HomeUiState()
}

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Cargando)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        cargarNegocios()
    }

    fun cargarNegocios() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Cargando
            NegocioRepository.getNegocios()
                .onSuccess { negocios ->
                    _uiState.value = HomeUiState.Exito(negocios)
                }
                .onFailure { error ->
                    _uiState.value = HomeUiState.Error(error.message ?: "Error al cargar negocios")
                }
        }
    }
}