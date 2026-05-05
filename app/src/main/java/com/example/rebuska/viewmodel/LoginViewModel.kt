package com.example.rebuska.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rebuska.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _cargando = MutableLiveData(false)
    val cargando: LiveData<Boolean> = _cargando

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _loginExitoso = MutableLiveData(false)
    val loginExitoso: LiveData<Boolean> = _loginExitoso

    fun iniciarSesion(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _error.value = "Completa todos los campos"
            return
        }

        _cargando.value = true
        _error.value    = null

        viewModelScope.launch {
            val result = AuthRepository.iniciarSesion(email, password)
            _cargando.value = false
            result.fold(
                onSuccess = { _loginExitoso.value = true },
                onFailure = { _error.value = AuthRepository.traducirError(it.message) }
            )
        }
    }

    fun limpiarError() { _error.value = null }
}