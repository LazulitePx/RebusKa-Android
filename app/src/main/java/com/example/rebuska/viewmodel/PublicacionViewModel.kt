package com.example.rebuska.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.rebuska.data.model.Publicacion
import com.example.rebuska.data.repository.PublicacionRepository

class PublicacionViewModel : ViewModel() {

    private val repo = PublicacionRepository
    val publicaciones = mutableStateListOf<Publicacion>()
    val cargando = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    // ───────────────────────────────
    // Cargar publicaciones por negocio
    // ───────────────────────────────
    fun cargarPublicaciones(idNegocio: String) {
        cargando.value = true
        repo.getPublicacionesPorNegocio(idNegocio) { lista ->
            publicaciones.clear()
            publicaciones.addAll(lista)
            cargando.value = false
        }
    }

    // ───────────────────────────────
    // Crear nueva publicación
    // ───────────────────────────────
    fun crearPublicacion(
        titulo: String,
        descripcion: String,
        precio: Long,
        tipo: String,
        idNegocio: String,
        imagenUri: Uri?
    ) {
        cargando.value = true
        repo.crearPublicacion(
            titulo = titulo,
            descripcion = descripcion,
            precio = precio,
            tipo = tipo,
            idNegocio = idNegocio,
            imagenUri = imagenUri
        ) { exito ->
            cargando.value = false
            if (exito) {
                cargarPublicaciones(idNegocio)
            } else {
                error.value = "Error al crear publicación"
            }
        }
    }

    // ───────────────────────────────
    // Editar publicación existente
    // ───────────────────────────────
    fun editarPublicacion(publicacion: Publicacion) {
        cargando.value = true
        repo.actualizarPublicacion(publicacion) { exito ->
            cargando.value = false
            if (exito) {
                val index = publicaciones.indexOfFirst { it.id == publicacion.id }
                if (index != -1) publicaciones[index] = publicacion
            } else {
                error.value = "Error al actualizar publicación"
            }
        }
    }

    // ───────────────────────────────
    // Eliminar publicación
    // ───────────────────────────────
    fun eliminarPublicacion(id: String) {
        cargando.value = true
        repo.eliminarPublicacion(id) { exito ->
            cargando.value = false
            if (exito) publicaciones.removeAll { it.id == id }
            else error.value = "Error al eliminar publicación"
        }
    }

    // ───────────────────────────────
    // Obtener publicación por ID
    // ───────────────────────────────
    fun obtenerPublicacionPorId(id: String, onResult: (Publicacion?) -> Unit) {
        repo.getPublicacionById(id) { publicacion ->
            onResult(publicacion)
        }
    }
}
