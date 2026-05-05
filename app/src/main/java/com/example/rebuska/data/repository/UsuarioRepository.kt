package com.example.rebuska.data.repository

import com.example.rebuska.data.model.Usuario
import com.example.rebuska.data.remote.FirestoreService

object UsuarioRepository {

    suspend fun getUsuarioActual(): Result<Usuario> =
        FirestoreService.getUsuarioActual()

    suspend fun crearUsuario(usuario: Usuario): Result<Unit> =
        FirestoreService.crearUsuario(usuario)

    suspend fun actualizarNombre(nuevoNombre: String, nuevoApellido: String): Result<Unit> =
        FirestoreService.actualizarUsuario(
            mapOf("nombre" to nuevoNombre, "apellido" to nuevoApellido)
        )

    suspend fun actualizarEmail(nuevoEmail: String): Result<Unit> =
        FirestoreService.actualizarUsuario(mapOf("email" to nuevoEmail))

    suspend fun actualizarTelefono(nuevoTelefono: String): Result<Unit> =
        FirestoreService.actualizarUsuario(mapOf("telefono" to nuevoTelefono))
}