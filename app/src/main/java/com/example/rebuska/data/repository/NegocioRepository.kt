package com.example.rebuska.data.repository

import com.example.rebuska.data.model.Negocio
import com.example.rebuska.data.remote.FirestoreService
import com.example.rebuska.data.remote.StorageService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object NegocioRepository {

    suspend fun getNegocios(): Result<List<Negocio>> =
        FirestoreService.getNegocios()

    suspend fun getNegocioById(id: String): Result<Negocio> =
        FirestoreService.getNegocioById(id)

    suspend fun getNegociosByTrabajador(idTrabajador: String): Result<List<Negocio>> =
        FirestoreService.getNegociosByTrabajador(idTrabajador)

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

        val negocioRef = FirestoreService.negociosCol.document()
        val negocioId = negocioRef.id

        logoBytes?.let {
            logoUrl = StorageService.uploadImage(it, "logos/$negocioId.jpg").getOrThrow()
        }
        bannerBytes?.let {
            bannerUrl = StorageService.uploadImage(it, "banners/$negocioId.jpg").getOrThrow()
        }

        val negocio = Negocio(
            id = negocioId,
            idTrabajador = idTrabajador,
            nombre = nombre,
            descripcion = descripcion,
            categoria = categoria,
            logoUrl = logoUrl,
            bannerUrl = bannerUrl
        )

        FirestoreService.crearNegocio(negocio).getOrThrow()
    }

    suspend fun actualizarNegocio(id: String, campos: Map<String, Any>): Result<Unit> =
        FirestoreService.actualizarNegocio(id, campos)

    suspend fun eliminarNegocio(id: String): Result<Unit> =
        FirestoreService.eliminarNegocio(id)
}