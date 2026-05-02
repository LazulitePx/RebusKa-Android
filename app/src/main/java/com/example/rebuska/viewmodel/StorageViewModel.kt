package com.example.rebuska.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rebuska.data.repository.StorageRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class StorageViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // ── Estado general ────────────────────────────────────
    private val _cargando  = MutableLiveData(false)
    val cargando: LiveData<Boolean> = _cargando

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _urlSubida = MutableLiveData<String?>()
    val urlSubida: LiveData<String?> = _urlSubida

    // ── Subir foto de perfil ──────────────────────────────
    // Sube la imagen y guarda la URL en Firestore automáticamente
    fun subirFotoPerfil(uid: String, uri: Uri) {
        _cargando.value = true
        viewModelScope.launch {
            val result = StorageRepository.subirFotoPerfil(uri)
            result.fold(
                onSuccess = { url ->
                    // Guarda la URL en el documento del usuario en Firestore
                    db.collection("usuarios").document(uid)
                        .update("fotoUrl", url)
                    _urlSubida.value = url
                    _cargando.value  = false
                },
                onFailure = {
                    _error.value    = it.message
                    _cargando.value = false
                }
            )
        }
    }

    // ── Subir logo del negocio ────────────────────────────
    fun subirLogoNegocio(negocioId: String, uri: Uri) {
        _cargando.value = true
        viewModelScope.launch {
            val result = StorageRepository.subirLogoNegocio(negocioId, uri)
            result.fold(
                onSuccess = { url ->
                    // Guarda la URL en el documento del negocio en Firestore
                    db.collection("negocios").document(negocioId)
                        .update("logoUrl", url)
                    _urlSubida.value = url
                    _cargando.value  = false
                },
                onFailure = {
                    _error.value    = it.message
                    _cargando.value = false
                }
            )
        }
    }

    // ── Subir banner del negocio ──────────────────────────
    fun subirBannerNegocio(negocioId: String, uri: Uri) {
        _cargando.value = true
        viewModelScope.launch {
            val result = StorageRepository.subirBannerNegocio(negocioId, uri)
            result.fold(
                onSuccess = { url ->
                    db.collection("negocios").document(negocioId)
                        .update("bannerUrl", url)
                    _urlSubida.value = url
                    _cargando.value  = false
                },
                onFailure = {
                    _error.value    = it.message
                    _cargando.value = false
                }
            )
        }
    }

    // ── Subir foto de publicación ─────────────────────────
    fun subirFotoPublicacion(negocioId: String, pubId: String, uri: Uri) {
        _cargando.value = true
        viewModelScope.launch {
            val result = StorageRepository.subirFotoPublicacion(pubId, uri)
            result.fold(
                onSuccess = { url ->
                    db.collection("negocios").document(negocioId)
                        .collection("publicaciones").document(pubId)
                        .update("fotoUrl", url)
                    _urlSubida.value = url
                    _cargando.value  = false
                },
                onFailure = {
                    _error.value    = it.message
                    _cargando.value = false
                }
            )
        }
    }

    fun limpiarEstado() {
        _error.value    = null
        _urlSubida.value = null
    }
}