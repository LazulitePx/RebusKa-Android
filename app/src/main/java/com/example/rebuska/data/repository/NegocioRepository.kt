package com.example.rebuska.data.repository
import com.example.rebuska.data.model.Negocio
import com.example.rebuska.R

object NegocioRepository {

    private val negocios = mutableListOf(
        Negocio(
            id = 1, idTrabajador = 1,
            nombre = "Carpintería López",
            descripcion = "Carpintería local con más de 20 años de experiencia. Especializada en muebles a medida, puertas, ventanas y restauración.",
            categoria = "Carpintería",
            promCalificacion = 4.7f, totalResenas = 128, verificado = true,
            logo = R.drawable.logo_carpinteria,
            banner = R.drawable.banner_carpinteria

        ),
        Negocio(
            id = 2, idTrabajador = 2,
            nombre = "Muebles Alta",
            descripcion = "Tienda especializada en muebles de alta gama para sala, comedor y alcoba. Diseños modernos y clásicos.",
            categoria = "Hogar",
            promCalificacion = 4.5f, totalResenas = 74, verificado = true,
            logo = R.drawable.logo_muebles,
            banner = R.drawable.banner_muebles
        ),
        Negocio(
            id = 3, idTrabajador = 3,
            nombre = "CompuTech",
            descripcion = "Tienda especializada en equipos gamer y tecnología de alta gama. Asesoría personalizada para armar tu setup ideal.",
            categoria = "Tecnología",
            promCalificacion = 4.8f, totalResenas = 210, verificado = true,
            logo = R.drawable.logo_computec,
            banner = R.drawable.banner_computec
        )
    )

    fun getNegocios(): List<Negocio> = negocios
    fun getNegocioById(id: Int): Negocio? = negocios.find { it.id == id }
    fun getNegociosByTrabajador(idTrabajador: Int): List<Negocio> = negocios.filter { it.idTrabajador == idTrabajador }

    fun actualizarNombre(idNegocio: Int, nuevoNombre: String) {
        val i = negocios.indexOfFirst { it.id == idNegocio }
        if (i != -1) negocios[i] = negocios[i].copy(nombre = nuevoNombre)
    }
    fun actualizarDescripcion(idNegocio: Int, nuevaDescripcion: String) {
        val i = negocios.indexOfFirst { it.id == idNegocio }
        if (i != -1) negocios[i] = negocios[i].copy(descripcion = nuevaDescripcion)
    }
}