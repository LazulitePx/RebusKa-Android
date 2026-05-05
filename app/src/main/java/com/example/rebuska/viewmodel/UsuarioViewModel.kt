package com.example.rebuska.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rebuska.data.model.Usuario
import com.example.rebuska.data.repository.UsuarioRepository

class UsuarioViewModel : ViewModel() {

    private val repository = UsuarioRepository()

    private val _usuario = MutableLiveData<Usuario?>()
    val usuario: LiveData<Usuario?> = _usuario

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _cargando = MutableLiveData<Boolean>()
    val cargando: LiveData<Boolean> = _cargando

    fun crearUsuario(usuario: Usuario) {
        _cargando.value = true
        repository.crearUsuario(
            usuario,
            onSuccess = { _cargando.value = false },
            onError   = { _cargando.value = false; _error.value = it.message }
        )
    }

    fun cargarUsuario(uid: String) {
        _cargando.value = true
        repository.obtenerUsuario(
            uid,
            onResult = { _cargando.value = false; _usuario.value = it },
            onError  = { _cargando.value = false; _error.value = it.message }
        )
    }
}