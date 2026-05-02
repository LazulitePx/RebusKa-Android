package com.example.rebuska.data.model

sealed class Usuario {
    abstract val id: String          // String para el ID de FireBase
    abstract val nombre: String
    abstract val apellido: String
    abstract val cedula: String
    abstract val email: String
    abstract val telefono: String
    abstract val fechaRegistro: Long
    abstract val tipo: String        // Campo nuevo para Firestore

    data class Cliente(
        override val id: String = "",
        override val nombre: String = "",
        override val apellido: String = "",
        override val cedula: String = "",
        override val email: String = "",
        override val telefono: String = "",
        override val fechaRegistro: Long = 0L,
        override val tipo: String = "cliente",
        val serviciosAdquiridos: List<String> = emptyList() // IDs de publicaciones
    ) : Usuario()

    data class Trabajador(
        override val id: String = "",
        override val nombre: String = "",
        override val apellido: String = "",
        override val cedula: String = "",
        override val email: String = "",
        override val telefono: String = "",
        override val fechaRegistro: Long = 0L,
        override val tipo: String = "trabajador",
        val negocios: List<String> = emptyList(), // IDs de negocios
        val verificado: Boolean = false
    ) : Usuario()
}