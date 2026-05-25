package com.example.rebuska.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rebuska.data.repository.AuthRepository
import com.example.rebuska.ui.screens.login.RolUsuario
import kotlinx.coroutines.launch

class RegistroViewModel : ViewModel() {

    // ── Estado de la UI ───────────────────────────────────
    private val _cargando = MutableLiveData(false)
    val cargando: LiveData<Boolean> = _cargando

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _registroExitoso = MutableLiveData(false)
    val registroExitoso: LiveData<Boolean> = _registroExitoso

    // ── Datos del formulario (persisten entre pantallas) ──
    var rol      = RolUsuario.CLIENTE
    var nombre   = ""
    var apellido = ""
    var email    = ""
    var cedula   = ""
    var telefono = ""
    var password = ""

    // ── Registro final ────────────────────────────────────
    fun registrar() {
        if (!validar()) return

        _cargando.value = true
        _error.value    = null

        val datos: Map<String, Any> = when (rol) {
            RolUsuario.CLIENTE -> mapOf(
                "tipo"                to "cliente",
                "nombre"              to nombre,
                "apellido"            to apellido,
                "cedula"              to cedula,
                "email"               to email,
                "telefono"            to telefono,
                "fechaRegistro"       to System.currentTimeMillis(),
                "serviciosAdquiridos" to emptyList<String>()
            )
            RolUsuario.TRABAJADOR -> mapOf(
                "tipo"          to "trabajador",
                "nombre"        to nombre,
                "apellido"      to apellido,
                "cedula"        to cedula,
                "email"         to email,
                "telefono"      to telefono,
                "fechaRegistro" to System.currentTimeMillis(),
                "negocios"      to emptyList<String>(),
                "verificado"    to false
            )
        }

        viewModelScope.launch {
            val result = AuthRepository.registrarUsuario(
                email        = email,
                password     = password,
                datosUsuario = datos
            )
            _cargando.value = false
            result.fold(
                onSuccess = { _registroExitoso.value = true },
                onFailure = { _error.value = AuthRepository.traducirError(it.message) }
            )
        }
    }

    fun limpiarError() { _error.value = null }

    private fun validar(): Boolean = when {
        nombre.isBlank()    -> { _error.value = "El nombre es requerido";             false }
        apellido.isBlank()  -> { _error.value = "El apellido es requerido";           false }
        email.isBlank()     -> { _error.value = "El correo es requerido";             false }
        cedula.isBlank()    -> { _error.value = "La cédula es requerida";             false }
        telefono.isBlank()  -> { _error.value = "El teléfono es requerido";           false }
        password.length < 6 -> { _error.value = "La contraseña debe tener mínimo 6 caracteres"; false }
        else                -> true
    }

    fun validarNombreCampo(texto: String): String? {

        val valor = texto.trim()

        if (valor.isEmpty()) {
            return "Campo obligatorio"
        }

        if (valor.length < 2) {
            return "Debe tener mínimo 2 letras"
        }

        val regex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")

        if (!regex.matches(valor)) {
            return "Solo se permiten letras"
        }

        if (valor.contains("  ")) {
            return "No se permiten espacios dobles"
        }

        return null
    }

    fun validarEmailCampo(email: String): String? {

        val valor = email.trim()

        if (valor.isEmpty()) {
            return "Correo obligatorio"
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(valor).matches()) {
            return "Correo inválido"
        }

        return null
    }

    fun validarCedulaCampo(cedula: String): String? {

        val valor = cedula.trim()

        if (valor.isEmpty()) {
            return "La cédula es obligatoria"
        }

        if (!valor.all { it.isDigit() }) {
            return "Solo se permiten números"
        }

        return null
    }

    fun validarCelularCampo(celular: String): String? {

        val valor = celular.trim()

        if (valor.isEmpty()) {
            return "El celular es obligatorio"
        }

        if (!valor.all { it.isDigit() }) {
            return "Solo se permiten números"
        }

        if (!valor.startsWith("3")) {
            return "Debe iniciar con 3"
        }

        if (valor.length != 10) {
            return "Debe tener exactamente 10 dígitos"
        }

        return null
    }

    fun validarPasswordCampo(password: String): String? {

        if (password.isBlank()) {
            return "La contraseña es obligatoria"
        }

        if (password.length < 8) {
            return "Mínimo 8 caracteres"
        }

        if (!password.any { it.isLetter() }) {
            return "Debe contener letras"
        }

        if (!password.any { it.isDigit() }) {
            return "Debe contener números"
        }

        val simbolos = Regex("[^A-Za-z0-9]")

        if (!simbolos.containsMatchIn(password)) {
            return "Debe contener un símbolo"
        }

        return null
    }

    fun validarConfirmarPassword(
        password: String,
        confirmar: String
    ): String? {

        if (confirmar.isBlank()) {
            return "Confirma tu contraseña"
        }

        if (password != confirmar) {
            return "Las contraseñas no coinciden"
        }

        return null
    }
}