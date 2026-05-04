package com.example.rebuska.data.model

data class Chat(
    val id: String = "",
    val idUsuario1: String = "",
    val idUsuario2: String = "",
    val nombreContacto: String = "",
    val ultimoMensaje: String = "",
    val timestamp: Long = 0L,
    val noLeidos: Int = 0,
    val fotoUrl: String = ""
)