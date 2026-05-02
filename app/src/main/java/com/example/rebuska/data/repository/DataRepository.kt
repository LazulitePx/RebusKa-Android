package com.example.rebuska.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

object StorageRepository {

    private val storage = FirebaseStorage.getInstance()
    private val auth    = FirebaseAuth.getInstance()

    // ══════════════════════════════════════════════════════
    // FOTO DE PERFIL DEL USUARIO
    // ══════════════════════════════════════════════════════
    // Ruta en Storage: usuarios/{uid}/perfil.jpg
    // ──────────────────────────────────────────────────────

    suspend fun subirFotoPerfil(imagenUri: Uri): Result<String> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(Exception("Usuario no autenticado"))

            val ref = storage.reference
                .child("usuarios/$uid/perfil.jpg")

            ref.putFile(imagenUri).await()          // sube el archivo
            val url = ref.downloadUrl.await()       // obtiene la URL pública
            Result.success(url.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ══════════════════════════════════════════════════════
    // LOGO DEL NEGOCIO
    // ══════════════════════════════════════════════════════
    // Ruta en Storage: negocios/{negocioId}/logo.jpg
    // ──────────────────────────────────────────────────────

    suspend fun subirLogoNegocio(negocioId: String, imagenUri: Uri): Result<String> {
        return try {
            val ref = storage.reference
                .child("negocios/$negocioId/logo.jpg")

            ref.putFile(imagenUri).await()
            val url = ref.downloadUrl.await()
            Result.success(url.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ══════════════════════════════════════════════════════
    // BANNER DEL NEGOCIO
    // ══════════════════════════════════════════════════════
    // Ruta en Storage: negocios/{negocioId}/banner.jpg
    // ──────────────────────────────────────────────────────

    suspend fun subirBannerNegocio(negocioId: String, imagenUri: Uri): Result<String> {
        return try {
            val ref = storage.reference
                .child("negocios/$negocioId/banner.jpg")

            ref.putFile(imagenUri).await()
            val url = ref.downloadUrl.await()
            Result.success(url.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ══════════════════════════════════════════════════════
    // FOTO DE PUBLICACIÓN
    // ══════════════════════════════════════════════════════
    // Ruta en Storage: publicaciones/{pubId}/foto.jpg
    // ──────────────────────────────────────────────────────

    suspend fun subirFotoPublicacion(pubId: String, imagenUri: Uri): Result<String> {
        return try {
            val ref = storage.reference
                .child("publicaciones/$pubId/foto.jpg")

            ref.putFile(imagenUri).await()
            val url = ref.downloadUrl.await()
            Result.success(url.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ══════════════════════════════════════════════════════
    // ELIMINAR IMAGEN
    // ══════════════════════════════════════════════════════

    suspend fun eliminarImagen(url: String): Result<Unit> {
        return try {
            // Obtiene la referencia directamente desde la URL pública
            val ref = storage.getReferenceFromUrl(url)
            ref.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}