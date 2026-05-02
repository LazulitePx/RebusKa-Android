package com.example.rebuska.data.model

data class Publicacion(

    val id: String = "",
    val idNegocio: String = "",
    val titulo: String,
    val descripcion: String,
    val precio: Long,
    val categoria: String,
    val tipo: TipoPublicacion,
    val foto: Int? = null
)

enum class TipoPublicacion {
    PRODUCTO,
    SERVICIO
}