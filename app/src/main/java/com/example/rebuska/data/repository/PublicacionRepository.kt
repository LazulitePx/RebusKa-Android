package com.example.rebuska.data.repository

import android.net.Uri
import com.example.rebuska.data.model.Publicacion
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

object PublicacionRepository {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance().reference
    private val publicacionesRef = db.collection("publicaciones")

    // ───────────────────────────────
    // CREAR PUBLICACION
    // ───────────────────────────────
    fun crearPublicacion(
        titulo: String,
        descripcion: String,
        precio: Long,
        tipo: String,
        idNegocio: String,
        imagenUri: Uri?,
        onComplete: (Boolean) -> Unit
    ) {
        val id = publicacionesRef.document().id

        fun guardarPublicacion(fotoUrl: String) {
            val data = mapOf(
                "id" to id,
                "titulo" to titulo,
                "descripcion" to descripcion,
                "precio" to precio,
                "tipo" to tipo,
                "idNegocio" to idNegocio,
                "fotoUrl" to fotoUrl
            )

            publicacionesRef.document(id)
                .set(data)
                .addOnSuccessListener { onComplete(true) }
                .addOnFailureListener { onComplete(false) }
        }

        if (imagenUri != null) {
            val ref = storage.child("publicaciones/$id.jpg")
            ref.putFile(imagenUri)
                .continueWithTask { ref.downloadUrl }
                .addOnSuccessListener { url -> guardarPublicacion(url.toString()) }
                .addOnFailureListener { onComplete(false) }
        } else {
            guardarPublicacion("")
        }
    }

    // ───────────────────────────────
    // OBTENER PUBLICACIONES POR NEGOCIO (SUSPEND)
    // ───────────────────────────────
    suspend fun getPublicacionesByNegocio(idNegocio: String): Result<List<Publicacion>> {
        return try {
            val snapshot = publicacionesRef
                .whereEqualTo("idNegocio", idNegocio)
                .get()
                .await()

            val publicaciones = snapshot.map { it.toObject(Publicacion::class.java).copy(id = it.id) }
            Result.success(publicaciones)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ───────────────────────────────
    // OBTENER PUBLICACIONES POR NEGOCIO (CALLBACK)
    // ───────────────────────────────
    fun getPublicacionesPorNegocio(
        idNegocio: String,
        onResult: (List<Publicacion>) -> Unit
    ) {
        publicacionesRef.whereEqualTo("idNegocio", idNegocio)
            .get()
            .addOnSuccessListener { result ->
                val lista = result.map { it.toObject(Publicacion::class.java).copy(id = it.id) }
                onResult(lista)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    // ───────────────────────────────
    // OBTENER PUBLICACION POR ID
    // ───────────────────────────────
    fun getPublicacionById(id: String, onResult: (Publicacion?) -> Unit) {
        publicacionesRef.document(id)
            .get()
            .addOnSuccessListener { doc ->
                val publicacion = doc.toObject(Publicacion::class.java)?.copy(id = doc.id)
                onResult(publicacion)
            }
            .addOnFailureListener { onResult(null) }
    }

    // ───────────────────────────────
    // ACTUALIZAR PUBLICACION
    // ───────────────────────────────
    fun actualizarPublicacion(publicacion: Publicacion, onComplete: (Boolean) -> Unit) {
        publicacionesRef.document(publicacion.id)
            .set(publicacion)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    // ───────────────────────────────
    // ELIMINAR PUBLICACION
    // ───────────────────────────────
    fun eliminarPublicacion(id: String, onComplete: (Boolean) -> Unit) {
        publicacionesRef.document(id)
            .delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}
