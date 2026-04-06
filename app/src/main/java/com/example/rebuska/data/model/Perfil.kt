package com.example.rebuska.data.model

data class Perfil (

    // logica mas adelante

        //val usuario: Usuario, puede ser trabajador o cliente
        val empresas: List<Negocio> = emptyList(),
        val publicaciones: List<Publicacion> = emptyList(),
        val descripcion: String,
        val valoracion: Double = 0.0,
        val reseñas: Int = 0,
        val trabajosRealizados: Int = 0

    )