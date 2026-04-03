package com.example.rebuska.data.model

data class Negocio(

    val id: Int,
    val idTrabajador: Int,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val promCalificacion: Float = 0f,
    val totalResenas: Int = 0,
    val verificado: Boolean = false,
    val logo: Int? = null,
    val banner: Int? = null

)
