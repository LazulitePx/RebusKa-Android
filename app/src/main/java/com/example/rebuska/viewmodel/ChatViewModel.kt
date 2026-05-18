package com.example.rebuska.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rebuska.data.model.Chat
import com.example.rebuska.data.model.Mensaje
import com.example.rebuska.data.remote.FirestoreService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.async

class ChatViewModel : ViewModel() {

    private val _mensajes = MutableStateFlow<List<Mensaje>>(emptyList())
    val mensajes: StateFlow<List<Mensaje>> = _mensajes

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats

    private val _chatId = MutableStateFlow("")
    val chatId: StateFlow<String> = _chatId

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando

    val uidActual = Firebase.auth.currentUser?.uid ?: ""

    private val _nombreContacto = MutableStateFlow("")
    val nombreContacto: StateFlow<String> = _nombreContacto

    private val _logoUrl = MutableStateFlow("")
    val logoUrl: StateFlow<String> = _logoUrl
    private val _datosListos = MutableStateFlow(false)
    val datosListos: StateFlow<Boolean> = _datosListos

    fun abrirChat(chatId: String) {
        if (chatId.isBlank()) return
        _chatId.value = chatId
        escucharMensajes(chatId)
        viewModelScope.launch {
            FirestoreService.resetearNoLeidos(chatId)
            val uid = Firebase.auth.currentUser?.uid ?: return@launch
            val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
            val doc = db.collection("chats").document(chatId).get().await()

            val idUsuario1 = doc.getString("idUsuario1") ?: ""
            val esCliente = uid == idUsuario1

            _nombreContacto.value = if (esCliente)
                doc.getString("nombreParaUsuario1") ?: "Chat"
            else
                doc.getString("nombreParaUsuario2") ?: "Chat"

            // Solo el cliente ve el logo — el trabajador ve iniciales
            _logoUrl.value = if (esCliente)
                doc.getString("logoUrl") ?: ""
            else
                ""  // vacío = mostrara iniciales en ChatScreen
        }
        _datosListos.value = true
    }

    private fun escucharMensajes(chatId: String) {
        FirestoreService.escucharMensajes(chatId) { nuevos ->
            _mensajes.value = nuevos
        }
    }

    fun enviarMensaje(texto: String) {
        if (texto.isBlank() || _chatId.value.isEmpty()) return
        viewModelScope.launch {
            FirestoreService.enviarMensaje(_chatId.value, texto)
        }
    }

    fun cargarChats() {
        viewModelScope.launch {
            FirestoreService.getChatsDelUsuario()
                .onSuccess { _chats.value = it }
        }
    }
    fun contactar(idTrabajador: String, nombreNegocio: String, logoUrl: String = "", onChatListo: (String) -> Unit) {
        viewModelScope.launch {
            _cargando.value = true
            FirestoreService.obtenerOCrearChat(idTrabajador, nombreNegocio, logoUrl)
                .onSuccess { id -> onChatListo(id) }
            _cargando.value = false
        }
    }
}