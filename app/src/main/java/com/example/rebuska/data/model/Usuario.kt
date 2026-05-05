package com.example.rebuska.data.model

data class Usuario(
    val id: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val cedula: String = "",
    val email: String = "",
    val telefono: String = "",
    val fechaRegistro: Long = 0L,
    val rol: String = "cliente",
    val verificado: Boolean = false,
    val serviciosAdquiridos: List<String> = emptyList(),
    val negocios: List<String> = emptyList()
)