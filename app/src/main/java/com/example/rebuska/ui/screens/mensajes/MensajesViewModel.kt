package com.example.rebuska.ui.screens.mensajes

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.rebuska.data.model.Chat

class MensajesViewModel : ViewModel() {

    private val _chats = mutableStateListOf<Chat>()
    val chats: List<Chat> = _chats

    init {
        cargarChats()
    }

    private fun cargarChats() {
        _chats.clear()
        _chats.addAll(
            listOf(
                Chat(
                    id = "1",
                    nombreContacto = "Carpintería López",
                    ultimoMensaje = "¡Claro! Con gusto ¿qué medidas necesitas?",
                    timestamp = System.currentTimeMillis()
                ),
                Chat(
                    id = "2",
                    nombreContacto = "Ultra Pinturas",
                    ultimoMensaje = "¿Cuántos m² tiene el apartamento?",
                    timestamp = System.currentTimeMillis() - 86400000
                ),
                Chat(
                    id = "3",
                    nombreContacto = "Solo techos y fachadas",
                    ultimoMensaje = "Buenas tardes estoy buscando baldosas",
                    timestamp = System.currentTimeMillis() - 172800000
                )
            )
        )
    }
}