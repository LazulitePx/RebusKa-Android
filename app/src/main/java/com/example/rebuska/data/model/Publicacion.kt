package com.example.rebuska.data.model

data class Publicacion(
    val id: String = "",
    val idNegocio: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val precio: Long = 0L,
    val categoria: String = "",
    val tipo: String = "PRODUCTO",       // "PRODUCTO" o "SERVICIO"
    val fotoUrl: String = ""
)