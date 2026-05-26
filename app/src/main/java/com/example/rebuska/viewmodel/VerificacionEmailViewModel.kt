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
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Job

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

    private val _segundosExpiracion = MutableStateFlow(300) // 5 min countdown
    val segundosExpiracion: StateFlow<Int> = _segundosExpiracion

    private val _segundosReenvio = MutableStateFlow(60)
    val segundosReenvio: StateFlow<Int> = _segundosReenvio

    private var jobExpiracion: Job? = null
    private var jobReenvio: Job? = null

    // Enviar codigo OTP al email
    fun enviarEmail(email: String) {

        viewModelScope.launch {

            _estado.value = EmailVerificacionState.Cargando

            val result = OtpRepository.enviarOtp(email)

            if (result.isSuccess) {

                _estado.value = EmailVerificacionState.Pendiente

                iniciarContadorExpiracion()
                iniciarContadorReenvio()

            } else {

                _estado.value = EmailVerificacionState.Error(
                    result.exceptionOrNull()?.message
                        ?: "Error enviando código de verificación"
                )
            }
        }
    }

    fun verificarCodigo(email: String, codigo: String) {
        viewModelScope.launch {
            _estado.value = EmailVerificacionState.Cargando

            val result = OtpRepository.verificarOtp(email, codigo)

            if (result.isSuccess) {
                // Recargar para obtener el emailVerified actualizado por el Admin SDK
                FirebaseAuth.getInstance().currentUser?.reload()?.await()

                val verificado = FirebaseAuth.getInstance().currentUser?.isEmailVerified ?: false

                _estado.value = if (verificado) {
                    EmailVerificacionState.Verificado
                } else {
                    // Si llegaste aquí, la Cloud Function no actualizó emailVerified
                    EmailVerificacionState.Error("El código es correcto pero no se pudo marcar el correo como verificado")
                }
            } else {
                _estado.value = EmailVerificacionState.Error(
                    result.exceptionOrNull()?.message ?: "Código incorrecto"
                )
            }
        }
    }

    fun reenviarCodigo(email: String) {

        viewModelScope.launch {

            _estado.value = EmailVerificacionState.Cargando

            val result = OtpRepository.enviarOtp(email)

            if (result.isSuccess) {

                _segundosExpiracion.value = 300   // reinicia expiración
                _segundosReenvio.value    = 60    // reinicia reenvío
                iniciarContadorExpiracion()
                iniciarContadorReenvio()

                _estado.value = EmailVerificacionState.Pendiente

            } else {

                _estado.value = EmailVerificacionState.Error(

                    result.exceptionOrNull()?.message
                        ?: "No se pudo reenviar el código"
                )
            }
        }
    }


    private fun iniciarContadorExpiracion() {
        jobExpiracion?.cancel()
        jobExpiracion = viewModelScope.launch {
            while (_segundosExpiracion.value > 0) {
                delay(1000)
                _segundosExpiracion.value--
            }
        }
    }

    private fun iniciarContadorReenvio() {
        jobReenvio?.cancel()
        jobReenvio = viewModelScope.launch {
            while (_segundosReenvio.value > 0) {
                delay(1000)
                _segundosReenvio.value--
            }
        }
    }
}