package com.example.rebuska.ui.screens.mensajes

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.rebuska.R
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
                    "Carpintería López",
                    "¡Claro! Con gusto ¿qué medidas necesitas?",
                    "6:40 PM",
                    R.drawable.logo_carpinteria
                ),
                Chat(
                    "Ultra Pinturas",
                    "¿Cuántos m² tiene el apartamento?",
                    "Dom",
                    R.drawable.logo_ultrapinturas
                ),
                Chat(
                    "Solo techos y fachadas",
                    "Buenas tardes estoy buscando baldosas",
                    "Sab",
                    R.drawable.logo_techos_fachadas
                ),
                Chat(
                    "AutoCar",
                    "Servicio completado ✅",
                    "Jue",
                    R.drawable.logo_autocar
                )
            )
        )
    }
}