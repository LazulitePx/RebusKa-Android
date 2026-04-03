package com.example.rebuska.data.model

sealed class Usuario {
    abstract val id: Int
    abstract val nombre: String
    abstract val apellido: String
    abstract val cedula: String
    abstract val email: String
    abstract val telefono: String
    abstract val fechaRegistro: Long

    data class Cliente(
        override val id: Int,
        override val nombre: String,
        override val apellido: String,
        override val cedula: String,
        override val email: String,
        override val telefono: String,
        override val fechaRegistro: Long,
        //Exclusivos del cliente
        val serviciosAdquiridos: List<String> = emptyList()
    ) : Usuario()

    data class Trabajador(
        override val id: Int,
        override val nombre: String,
        override val apellido: String,
        override val cedula: String,
        override val email: String,
        override val telefono: String,
        override val fechaRegistro: Long,
        //Exclusivos del trabajador
        val negocios: List<String> = emptyList(),
        val verificado: Boolean = false
    ) : Usuario()

}