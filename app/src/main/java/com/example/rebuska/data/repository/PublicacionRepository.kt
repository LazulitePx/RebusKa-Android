package com.example.rebuska.data.repository

import com.example.rebuska.data.model.Publicacion
import com.example.rebuska.data.remote.FirestoreService

object PublicacionRepository {

    suspend fun getPublicaciones(): Result<List<Publicacion>> =
        FirestoreService.getPublicaciones()

    suspend fun getPublicacionesByNegocio(idNegocio: String): Result<List<Publicacion>> =
        FirestoreService.getPublicacionesByNegocio(idNegocio)

    suspend fun crearPublicacion(publicacion: Publicacion): Result<String> =
        FirestoreService.crearPublicacion(publicacion)

    suspend fun actualizarPublicacion(
        id: String,
        titulo: String,
        descripcion: String,
        precio: Long
    ): Result<Unit> =
        FirestoreService.actualizarPublicacion(
            id, mapOf(
                "titulo"      to titulo,
                "descripcion" to descripcion,
                "precio"      to precio
            )
        )

    suspend fun eliminarPublicacion(id: String): Result<Unit> =
        FirestoreService.eliminarPublicacion(id)
}