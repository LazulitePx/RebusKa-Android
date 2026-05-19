package com.example.rebuska.data.model

data class Perfil(
        val id: String = "",
        val idUsuario: String = "",
        val descripcion: String = "",
        val valoracion: Double = 0.0,
        val resenas: Int = 0,
        val trabajosRealizados: Int = 0,
        val fotoUrl: String = ""
)