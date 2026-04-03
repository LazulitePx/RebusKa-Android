package com.example.rebuska.data.repository

import com.example.rebuska.data.model.Usuario

object UsuarioRepository {

    private var usuarioActual: Usuario = Usuario.Trabajador(
        id = 1,
        nombre = "Carlos",
        apellido = "López",
        cedula = "1098765432",
        email = "carlos.lopez@gmail.com",
        telefono = "3001234567",
        fechaRegistro = System.currentTimeMillis(),
        negocios = listOf("1"),
        verificado = true
    )

    fun getUsuarioActual(): Usuario = usuarioActual

    fun actualizarNombre(nuevoNombre: String, nuevoApellido: String) {
        usuarioActual = when (val u = usuarioActual) {
            is Usuario.Cliente    -> u.copy(nombre = nuevoNombre, apellido = nuevoApellido)
            is Usuario.Trabajador -> u.copy(nombre = nuevoNombre, apellido = nuevoApellido)
        }
    }

    fun actualizarEmail(nuevoEmail: String) {
        usuarioActual = when (val u = usuarioActual) {
            is Usuario.Cliente    -> u.copy(email = nuevoEmail)
            is Usuario.Trabajador -> u.copy(email = nuevoEmail)
        }
    }

    fun actualizarTelefono(nuevoTelefono: String) {
        usuarioActual = when (val u = usuarioActual) {
            is Usuario.Cliente    -> u.copy(telefono = nuevoTelefono)
            is Usuario.Trabajador -> u.copy(telefono = nuevoTelefono)
        }
    }
}