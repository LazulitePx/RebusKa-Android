package com.example.rebuska.data.repository

import com.example.rebuska.data.model.Negocio
import com.example.rebuska.data.remote.FirestoreService
import com.example.rebuska.data.remote.StorageService // Importa el nuevo StorageService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object NegocioRepository {

    suspend fun getNegocios(): Result<List<Negocio>> =
        FirestoreService.getNegocios()

    suspend fun getNegocioById(id: String): Result<Negocio> =
        FirestoreService.getNegocioById(id)

    suspend fun getNegociosByTrabajador(idTrabajador: String): Result<List<Negocio>> =
        FirestoreService.getNegociosByTrabajador(idTrabajador)

    // Modifica esta función para aceptar bytes y gestionar la subida
    suspend fun crearNegocio(
        nombre: String,
        descripcion: String,
        categoria: String,
        logoBytes: ByteArray?,
        bannerBytes: ByteArray?
    ): Result<String> = runCatching {
        val auth = Firebase.auth
        val idTrabajador = auth.currentUser?.uid ?: error("No hay sesión activa para crear un negocio")

        var logoUrl = ""
        var bannerUrl = ""

        // Crear una referencia para el nuevo documento de negocio para obtener su ID primero
        val negocioRef = FirestoreService.negociosCol.document()
        val negocioId = negocioRef.id

        // Subir logo a Firebase Storage
        logoBytes?.let {
            val logoUploadResult = StorageService.uploadImage(it, "logos/$negocioId.jpg")
            logoUrl = logoUploadResult.getOrThrow() // Lanzará una excepción si falla la subida
        }

        // Subir banner a Firebase Storage
        bannerBytes?.let {
            val bannerUploadResult = StorageService.uploadImage(it, "banners/$negocioId.jpg")
            bannerUrl = bannerUploadResult.getOrThrow() // Lanzará una excepción si falla la subida
        }

        // Crear el objeto Negocio con las URLs de descarga
        val negocio = Negocio(
            id = negocioId,
            idTrabajador = idTrabajador,
            nombre = nombre,
            descripcion = descripcion,
            categoria = categoria,
            logoUrl = logoUrl,
            bannerUrl = bannerUrl
        )

        // Guardar el negocio en Firestore usando la función que ya existe
        FirestoreService.crearNegocio(negocio).getOrThrow()
    }

    suspend fun actualizarNegocio(id: String, campos: Map<String, Any>): Result<Unit> =
        FirestoreService.actualizarNegocio(id, campos)

    suspend fun eliminarNegocio(id: String): Result<Unit> =
        FirestoreService.eliminarNegocio(id)
}