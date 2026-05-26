package com.example.rebuska.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rebuska.data.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class TelefonoVerificacionState {
    object Idle       : TelefonoVerificacionState()
    object Cargando   : TelefonoVerificacionState()
    object SmsSent    : TelefonoVerificacionState()
    object Verificado : TelefonoVerificacionState()
    data class Error(val mensaje: String) : TelefonoVerificacionState()
}

class VerificacionTelefonoViewModel : ViewModel() {

    private val _estado = MutableStateFlow<TelefonoVerificacionState>(TelefonoVerificacionState.Idle)
    val estado: StateFlow<TelefonoVerificacionState> = _estado

    private val _segundos = MutableStateFlow(60)
    val segundos: StateFlow<Int> = _segundos

    // El verificationId que devuelve Firebase al enviar el SMS
    private var verificationId: String = ""

    fun enviarSMS(telefono: String, activity: Activity) {
        // Guard: valida E.164 antes de llamar a Firebase
        if (!telefono.matches(Regex("^\\+[1-9]\\d{7,14}$"))) {
            _estado.value = TelefonoVerificacionState.Error(
                "Número inválido: $telefono"
            )
            return
        }

        _estado.value = TelefonoVerificacionState.Cargando
        AuthRepository.iniciarVerificacionTelefono(
            telefono     = telefono,
            activity     = activity,
            onCodeSent   = { id ->
                verificationId = id
                _estado.value = TelefonoVerificacionState.SmsSent
                iniciarContador()
            },
            onVerificado = {
                _estado.value = TelefonoVerificacionState.Verificado
            },
            onError = { e ->
                _estado.value = TelefonoVerificacionState.Error(
                    e.message ?: "Error al enviar el SMS"
                )
            }
        )
    }


    fun verificarCodigo(codigo: String) {
        viewModelScope.launch {
            _estado.value = TelefonoVerificacionState.Cargando
            val result = AuthRepository.verificarCodigoSMS(verificationId, codigo)
            _estado.value = if (result.isSuccess) {
                TelefonoVerificacionState.Verificado
            } else {
                TelefonoVerificacionState.Error(
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