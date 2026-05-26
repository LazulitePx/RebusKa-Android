package com.example.rebuska.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rebuska.data.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.rebuska.data.repository.OtpRepository
import com.google.firebase.auth.FirebaseAuth

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

    // Enviar codigo OTP al email
    fun enviarEmail(email: String) {

        viewModelScope.launch {

            _estado.value = EmailVerificacionState.Cargando

            val result = OtpRepository.enviarOtp(email)

            if (result.isSuccess) {

                _estado.value = EmailVerificacionState.Pendiente

                iniciarContador()

            } else {

                _estado.value = EmailVerificacionState.Error(
                    result.exceptionOrNull()?.message
                        ?: "Error enviando OTP"
                )
            }
        }
    }

    fun verificarCodigo(
        email: String,
        codigo: String
    ) {

        viewModelScope.launch {

            _estado.value =
                EmailVerificacionState.Cargando

            val result =
                OtpRepository.verificarOtp(
                    email,
                    codigo
                )

            if (result.isSuccess) {

                // Recargar usuario Firebase
                FirebaseAuth
                    .getInstance()
                    .currentUser
                    ?.reload()

                // Revisar si quedó verificado
                val verificado =
                    FirebaseAuth
                        .getInstance()
                        .currentUser
                        ?.isEmailVerified
                        ?: false

                if (verificado) {

                    _estado.value =
                        EmailVerificacionState.Verificado

                } else {

                    _estado.value =
                        EmailVerificacionState.Error(
                            "No se pudo verificar el correo"
                        )
                }

            } else {

                _estado.value =
                    EmailVerificacionState.Error(
                        result.exceptionOrNull()?.message
                            ?: "Código incorrecto"
                    )
            }
        }
    }

    fun reenviarCodigo(email: String) {

        viewModelScope.launch {

            _estado.value = EmailVerificacionState.Cargando

            val result = OtpRepository.enviarOtp(email)

            if (result.isSuccess) {

                _segundos.value = 300
                iniciarContador()

                _estado.value = EmailVerificacionState.Pendiente

            } else {

                _estado.value = EmailVerificacionState.Error(

                    result.exceptionOrNull()?.message
                        ?: "No se pudo reenviar el código"
                )
            }
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