package com.example.rebuska.data.model

data class Negocio(
    val id: String = "",
    val idTrabajador: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val categoria: String = "",
    val promCalificacion: Float = 0f,
    val totalResenas: Int = 0,
    val verificado: Boolean = false,
    val logoUrl: String = "",
    val bannerUrl: String = ""
)
