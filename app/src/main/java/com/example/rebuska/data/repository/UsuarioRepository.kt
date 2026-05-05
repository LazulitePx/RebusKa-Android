package com.example.rebuska.data.repository

import com.example.rebuska.data.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UsuarioRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val coleccion = db.collection("usuarios")

    // ── CREATE ────────────────────────────────────────────
    fun crearUsuario(usuario: Usuario, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val uid = auth.currentUser?.uid ?: return

        val data = when (usuario) {
            is Usuario.Cliente -> mapOf(
                "tipo" to "cliente",
                "nombre" to usuario.nombre,
                "apellido" to usuario.apellido,
                "cedula" to usuario.cedula,
                "email" to usuario.email,
                "telefono" to usuario.telefono,
                "fechaRegistro" to System.currentTimeMillis(),
                "serviciosAdquiridos" to emptyList<String>()
            )
            is Usuario.Trabajador -> mapOf(
                "tipo" to "trabajador",
                "nombre" to usuario.nombre,
                "apellido" to usuario.apellido,
                "cedula" to usuario.cedula,
                "email" to usuario.email,
                "telefono" to usuario.telefono,
                "fechaRegistro" to System.currentTimeMillis(),
                "negocios" to emptyList<String>(),
                "verificado" to false
            )
        }

        coleccion.document(uid).set(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    // ── READ ──────────────────────────────────────────────
    fun obtenerUsuario(uid: String, onResult: (Usuario?) -> Unit, onError: (Exception) -> Unit) {
        coleccion.document(uid).get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) { onResult(null); return@addOnSuccessListener }
                val usuario = when (doc.getString("tipo")) {
                    "trabajador" -> doc.toObject(Usuario.Trabajador::class.java)
                    "cliente"    -> doc.toObject(Usuario.Cliente::class.java)
                    else         -> null
                }
                onResult(usuario)
            }
            .addOnFailureListener { onError(it) }
    }

    // ── UPDATE ────────────────────────────────────────────
    fun actualizarCampo(uid: String, campo: String, valor: Any,
                        onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        coleccion.document(uid).update(campo, valor)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    // ── DELETE ────────────────────────────────────────────
    fun eliminarUsuario(uid: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        coleccion.document(uid).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }
}