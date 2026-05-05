package com.example.rebuska.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rebuska.data.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class CelularVerificacionState {
    object Idle       : CelularVerificacionState()
    object Cargando   : CelularVerificacionState()
    object SmsSent    : CelularVerificacionState()
    object Verificado : CelularVerificacionState()
    data class Error(val mensaje: String) : CelularVerificacionState()
}

class VerificacionCelularViewModel : ViewModel() {

    private val _estado = MutableStateFlow<CelularVerificacionState>(CelularVerificacionState.Idle)
    val estado: StateFlow<CelularVerificacionState> = _estado

    private val _segundos = MutableStateFlow(60)
    val segundos: StateFlow<Int> = _segundos

    // El verificationId que devuelve Firebase al enviar el SMS
    private var verificationId: String = ""

    fun enviarSMS(telefono: String, activity: Activity) {
        _estado.value = CelularVerificacionState.Cargando
        AuthRepository.iniciarVerificacionTelefono(
            telefono  = telefono,  // "+573001234567"
            activity  = activity,
            onCodeSent = { id ->
                verificationId = id
                _estado.value = CelularVerificacionState.SmsSent
                iniciarContador()
            },
            onError = { e ->
                _estado.value = CelularVerificacionState.Error(
                    e.message ?: "Error al enviar el SMS"
                )
            }
        )
    }

    fun verificarCodigo(codigo: String) {
        viewModelScope.launch {
            _estado.value = CelularVerificacionState.Cargando
            val result = AuthRepository.verificarCodigoSMS(verificationId, codigo)
            _estado.value = if (result.isSuccess) {
                CelularVerificacionState.Verificado
            } else {
                CelularVerificacionState.Error(
                    result.exceptionOrNull()?.message ?: "Código incorrecto"
                )
            }
        }
    }

    fun reenviarSMS(telefono: String, activity: Activity) {
        _segundos.value = 60
        enviarSMS(telefono, activity)
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