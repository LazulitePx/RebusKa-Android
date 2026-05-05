package com.example.rebuska.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rebuska.data.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class EmailVerificacionState {
    object Idle       : EmailVerificacionState()
    object Cargando   : EmailVerificacionState()
    object Verificado : EmailVerificacionState()
    object Pendiente  : EmailVerificacionState()  // email enviado, esperando click
    data class Error(val mensaje: String) : EmailVerificacionState()
}

class VerificacionEmailViewModel : ViewModel() {

    private val _estado = MutableStateFlow<EmailVerificacionState>(EmailVerificacionState.Idle)
    val estado: StateFlow<EmailVerificacionState> = _estado

    private val _segundos = MutableStateFlow(300) // 5 min countdown
    val segundos: StateFlow<Int> = _segundos

    // Llama esto después de crear la cuenta en Registro2
    fun enviarEmail(email: String, password: String) {
        viewModelScope.launch {
            _estado.value = EmailVerificacionState.Cargando
            val result = AuthRepository.enviarVerificacionEmail(email, password)
            if (result.isSuccess) {
                _estado.value = EmailVerificacionState.Pendiente
                iniciarContador()
            } else {
                _estado.value = EmailVerificacionState.Error(
                    result.exceptionOrNull()?.message ?: "Error al enviar el correo"
                )
            }
        }
    }

    // El usuario dice "ya verifiqué" — revisamos Firebase
    fun comprobarVerificacion() {
        viewModelScope.launch {
            _estado.value = EmailVerificacionState.Cargando
            val result = AuthRepository.verificarEmail()
            _estado.value = when {
                result.isSuccess && result.getOrDefault(false) -> EmailVerificacionState.Verificado
                result.isSuccess -> EmailVerificacionState.Error("Aún no has verificado tu correo. Revisa tu bandeja.")
                else -> EmailVerificacionState.Error(result.exceptionOrNull()?.message ?: "Error")
            }
        }
    }

    fun reenviarEmail() {
        viewModelScope.launch {
            AuthRepository.reenviarVerificacionEmail()
            _segundos.value = 300
            iniciarContador()
        }
    }

    private fun iniciarContador() {
        viewModelScope.launch {
            while (_segundos.value > 0) {
                delay(1000)
                _segundos.value--
            }
        }
    }
}