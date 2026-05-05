package com.example.rebuska.data.remote

import com.example.rebuska.data.model.Negocio
import com.example.rebuska.data.model.Perfil
import com.example.rebuska.data.model.Publicacion
import com.example.rebuska.data.model.Usuario
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import com.example.rebuska.data.model.Chat
import com.example.rebuska.data.model.Mensaje
import com.google.firebase.firestore.Query

object FirestoreService {

    private val db   = Firebase.firestore
    private val auth = Firebase.auth

    private val usuariosCol      = db.collection("usuarios")
    private val negociosCol      = db.collection("negocios")
    private val publicacionesCol = db.collection("publicaciones")
    private val perfilesCol      = db.collection("perfiles")

    // ════════════════════════════════════════════════════
    // USUARIO
    // ════════════════════════════════════════════════════

    suspend fun crearUsuario(datos: Map<String, Any>): Result<Unit> = runCatching {
        val uid = auth.currentUser?.uid ?: error("No hay sesión activa")
        usuariosCol.document(uid).set(datos).await()
    }

    suspend fun getUsuarioActual(): Result<Map<String, Any>> = runCatching {
        val uid = auth.currentUser?.uid ?: error("No hay sesión activa")
        val doc = usuariosCol.document(uid).get().await()
        @Suppress("UNCHECKED_CAST")
        doc.data as? Map<String, Any> ?: error("Usuario no encontrado")
    }

    suspend fun actualizarUsuario(campos: Map<String, Any>): Result<Unit> = runCatching {
        val uid = auth.currentUser?.uid ?: error("No hay sesión activa")
        usuariosCol.document(uid).update(campos).await()
    }

    // ════════════════════════════════════════════════════
    // NEGOCIO
    // ════════════════════════════════════════════════════

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
            .get().await().documents.map { doc ->
                doc.toObject(Negocio::class.java)!!.copy(id = doc.id)
            }
    }

    suspend fun actualizarNegocio(id: String, campos: Map<String, Any>): Result<Unit> = runCatching {
        negociosCol.document(id).update(campos).await()
    }

    suspend fun eliminarNegocio(id: String): Result<Unit> = runCatching {
        negociosCol.document(id).delete().await()
    }

    // ════════════════════════════════════════════════════
    // PUBLICACION
    // ════════════════════════════════════════════════════

    suspend fun crearPublicacion(publicacion: Publicacion): Result<String> = runCatching {
        val ref = publicacionesCol.document()
        val id  = ref.id
        ref.set(publicacion.copy(id = id)).await()
        id
    }

    suspend fun getPublicaciones(): Result<List<Publicacion>> = runCatching {
        publicacionesCol.get().await().documents.map { doc ->
            doc.toObject(Publicacion::class.java)!!.copy(id = doc.id)
        }
    }

    suspend fun getPublicacionesByNegocio(idNegocio: String): Result<List<Publicacion>> = runCatching {
        publicacionesCol
            .whereEqualTo("idNegocio", idNegocio)
            .get().await().documents.map { doc ->
                doc.toObject(Publicacion::class.java)!!.copy(id = doc.id)
            }
    }

    suspend fun actualizarPublicacion(id: String, campos: Map<String, Any>): Result<Unit> = runCatching {
        publicacionesCol.document(id).update(campos).await()
    }

    suspend fun eliminarPublicacion(id: String): Result<Unit> = runCatching {
        publicacionesCol.document(id).delete().await()
    }

    // ════════════════════════════════════════════════════
    // PERFIL
    // ════════════════════════════════════════════════════

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

    // ════════════════════════════════════════════════════
    // CHATS
    // ════════════════════════════════════════════════════

    private val chatsCol = db.collection("chats")

    suspend fun obtenerOCrearChat(
        idTrabajador: String,
        nombreNegocio: String
    ): Result<String> = runCatching {
        val idCliente = auth.currentUser?.uid ?: error("No hay sesión activa")

        // Buscar si ya existe un chat entre estos dos
        val existente = chatsCol
            .whereEqualTo("idUsuario1", idCliente)
            .whereEqualTo("idUsuario2", idTrabajador)
            .get().await()

        if (!existente.isEmpty) {
            return@runCatching existente.documents.first().id
        }

        // Crear nuevo chat
        val ref = chatsCol.document()
        ref.set(mapOf(
            "id"             to ref.id,
            "idUsuario1"     to idCliente,
            "idUsuario2"     to idTrabajador,
            "nombreContacto" to nombreNegocio,
            "ultimoMensaje"  to "",
            "timestamp"      to System.currentTimeMillis(),
            "noLeidos"       to 0
        )).await()

        ref.id
    }

    suspend fun getChatsDelUsuario(): Result<List<Chat>> = runCatching {
        val uid = auth.currentUser?.uid ?: error("No hay sesión activa")

        val comoCliente    = chatsCol.whereEqualTo("idUsuario1", uid).get().await()
        val comoTrabajador = chatsCol.whereEqualTo("idUsuario2", uid).get().await()

        val todos = (comoCliente.documents + comoTrabajador.documents)
            .distinctBy { it.id }
            .mapNotNull { doc ->
                val idUsuario1 = doc.getString("idUsuario1") ?: ""
                // Leer el campo de no leidos correcto segun el usuario actual
                val noLeidos = if (uid == idUsuario1)
                    doc.getLong("noLeidosUsuario1")?.toInt() ?: 0
                else
                    doc.getLong("noLeidosUsuario2")?.toInt() ?: 0

                Chat(
                    id             = doc.id,
                    idUsuario1     = idUsuario1,
                    idUsuario2     = doc.getString("idUsuario2") ?: "",
                    nombreContacto = doc.getString("nombreContacto") ?: "",
                    ultimoMensaje  = doc.getString("ultimoMensaje") ?: "",
                    timestamp      = doc.getLong("timestamp") ?: 0L,
                    noLeidos       = noLeidos
                )
            }
            .sortedByDescending { it.timestamp }

        todos
    }

    fun escucharMensajes(
        chatId: String,
        onChange: (List<Mensaje>) -> Unit
    ) {
        chatsCol.document(chatId)
            .collection("mensajes")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, _ ->
                val mensajes = snapshot?.documents?.mapNotNull { doc ->
                    Mensaje(
                        id        = doc.id,
                        idEmisor  = doc.getString("idEmisor") ?: "",
                        texto     = doc.getString("texto") ?: "",
                        timestamp = doc.getLong("timestamp") ?: 0L
                    )
                } ?: emptyList()
                onChange(mensajes)
            }
    }

    suspend fun enviarMensaje(chatId: String, texto: String): Result<Unit> = runCatching {
        val uid = auth.currentUser?.uid ?: error("No hay sesión activa")

        val ref = chatsCol.document(chatId).collection("mensajes").document()
        ref.set(mapOf(
            "id"        to ref.id,
            "idEmisor"  to uid,
            "texto"     to texto,
            "timestamp" to System.currentTimeMillis()
        )).await()

        // Determinar quién es el receptor
        val chatDoc = chatsCol.document(chatId).get().await()
        val idUsuario1 = chatDoc.getString("idUsuario1") ?: ""

        // Incrementar solo el campo del receptor
        val campoNoLeidos = if (uid == idUsuario1) "noLeidosUsuario2" else "noLeidosUsuario1"

        chatsCol.document(chatId).update(mapOf(
            "ultimoMensaje" to texto,
            "timestamp"     to System.currentTimeMillis(),
            campoNoLeidos   to com.google.firebase.firestore.FieldValue.increment(1)
        )).await()
    }
    suspend fun resetearNoLeidos(chatId: String): Result<Unit> = runCatching {
        val uid = auth.currentUser?.uid ?: error("No hay sesión activa")
        val chatDoc = chatsCol.document(chatId).get().await()
        val idUsuario1 = chatDoc.getString("idUsuario1") ?: ""
        val campo = if (uid == idUsuario1) "noLeidosUsuario1" else "noLeidosUsuario2"
        chatsCol.document(chatId).update(campo, 0).await()
    }

}

