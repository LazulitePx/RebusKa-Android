
package com.example.rebuska.data.model
data class Negocio(
    val id: String = "",             // String para Firebase
    val idTrabajador: String = "",   // String para Firebase
    val nombre: String = "",
    val descripcion: String = "",
    val categoria: String = "",
    val promCalificacion: Float = 0f,
    val totalResenas: Int = 0,
    val verificado: Boolean = false,
    val logoUrl: String? = null,     // ← URL de Firebase Storage
    val bannerUrl: String? = null    // ← URL de Firebase Storage
)