package com.example.rebuska.ui.screens.publicacion

data class PublicacionDisplay(
    val id: String,
    val titulo: String,
    val tiempoPublicacion: String,
    val descripcion: String,
    val imagenUrl: String // URL de Firebase Storage
)
