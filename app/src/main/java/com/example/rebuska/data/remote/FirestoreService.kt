package com.example.rebuska.data.remote

import com.example.rebuska.data.model.Negocio
import com.example.rebuska.data.model.Perfil
import com.example.rebuska.data.model.Publicacion
import com.example.rebuska.data.model.Usuario
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object FirestoreService {

    private val db   = Firebase.firestore
    private val auth = Firebase.auth

    // ── Colecciones ──────────────────────────────────────────────────────────
    private val usuariosCol    = db.collection("usuarios")
    private val negociosCol    = db.collection("negocios")
    private val publicacionesCol = db.collection("publicaciones")
    private val perfilesCol    = db.collection("perfiles")

    // ════════════════════════════════════════════════════════════════════════
    // USUARIO
    // ════════════════════════════════════════════════════════════════════════

    suspend fun crearUsuario(usuario: Usuario): Result<Unit> = runCatching {
        val uid = auth.currentUser?.uid ?: error("No hay sesión activa")
        usuariosCol.document(uid).set(usuario.copy(id = uid)).await()
    }

    suspend fun getUsuarioActual(): Result<Usuario> = runCatching {
        val uid = auth.currentUser?.uid ?: error("No hay sesión activa")
        usuariosCol.document(uid).get().await().toObject(Usuario::class.java)
            ?: error("Usuario no encontrado")
    }

    suspend fun actualizarUsuario(campos: Map<String, Any>): Result<Unit> = runCatching {
        val uid = auth.currentUser?.uid ?: error("No hay sesión activa")
        usuariosCol.document(uid).update(campos).await()
    }

    // ════════════════════════════════════════════════════════════════════════
    // NEGOCIO
    // ════════════════════════════════════════════════════════════════════════

    suspend fun crearNegocio(negocio: Negocio): Result<String> = runCatching {
        val ref = negociosCol.document()
        val id  = ref.id
        ref.set(negocio.copy(id = id)).await()
        id
    }

    suspend fun getNegocios(): Result<List<Negocio>> = runCatching {
        negociosCol.get().await().documents.map { doc ->
            doc.toObject(Negocio::class.java)!!.copy(id = doc.id)
        }
    }

    suspend fun getNegocioById(id: String): Result<Negocio> = runCatching {
        val doc = negociosCol.document(id).get().await()
        doc.toObject(Negocio::class.java)?.copy(id = doc.id)
            ?: error("Negocio no encontrado")
    }

    suspend fun getNegociosByTrabajador(idTrabajador: String): Result<List<Negocio>> = runCatching {
        negociosCol
            .whereEqualTo("idTrabajador", idTrabajador)
            .get().await()
            .toObjects(Negocio::class.java)
    }

    suspend fun actualizarNegocio(id: String, campos: Map<String, Any>): Result<Unit> = runCatching {
        negociosCol.document(id).update(campos).await()
    }

    suspend fun eliminarNegocio(id: String): Result<Unit> = runCatching {
        negociosCol.document(id).delete().await()
    }

    // ════════════════════════════════════════════════════════════════════════
    // PUBLICACION
    // ════════════════════════════════════════════════════════════════════════

    suspend fun crearPublicacion(publicacion: Publicacion): Result<String> = runCatching {
        val ref = publicacionesCol.document()
        val id  = ref.id
        ref.set(publicacion.copy(id = id)).await()
        id
    }

    suspend fun getPublicaciones(): Result<List<Publicacion>> = runCatching {
        publicacionesCol.get().await().toObjects(Publicacion::class.java)
    }

    suspend fun getPublicacionesByNegocio(idNegocio: String): Result<List<Publicacion>> = runCatching {
        publicacionesCol
            .whereEqualTo("idNegocio", idNegocio)
            .get().await()
            .toObjects(Publicacion::class.java)
    }

    suspend fun actualizarPublicacion(id: String, campos: Map<String, Any>): Result<Unit> = runCatching {
        publicacionesCol.document(id).update(campos).await()
    }

    suspend fun eliminarPublicacion(id: String): Result<Unit> = runCatching {
        publicacionesCol.document(id).delete().await()
    }

    // ════════════════════════════════════════════════════════════════════════
    // PERFIL
    // ════════════════════════════════════════════════════════════════════════

    suspend fun crearPerfil(perfil: Perfil): Result<Unit> = runCatching {
        val uid = auth.currentUser?.uid ?: error("No hay sesión activa")
        perfilesCol.document(uid).set(perfil.copy(id = uid, idUsuario = uid)).await()
    }

    suspend fun getPerfilActual(): Result<Perfil> = runCatching {
        val uid = auth.currentUser?.uid ?: error("No hay sesión activa")
        perfilesCol.document(uid).get().await().toObject(Perfil::class.java)
            ?: error("Perfil no encontrado")
    }

    suspend fun actualizarPerfil(campos: Map<String, Any>): Result<Unit> = runCatching {
        val uid = auth.currentUser?.uid ?: error("No hay sesión activa")
        perfilesCol.document(uid).update(campos).await()
    }
}