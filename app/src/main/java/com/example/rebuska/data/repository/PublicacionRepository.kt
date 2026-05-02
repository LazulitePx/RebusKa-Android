package com.example.rebuska.data.repository

import com.example.rebuska.data.model.Publicacion
import com.example.rebuska.data.model.TipoPublicacion

object PublicacionRepository {

    private val publicaciones = mutableListOf(

        // ── Negocio 1 ──────────────────────────────────────────────────────────
        Publicacion(
            id = "1", idNegocio = "1",
            titulo = "Muebles a medida para sala",
            descripcion = "Diseño y fabricación de muebles personalizados en madera maciza. Acabados finos, entrega en 15 días.",
            precio = 350_000, categoria = "Carpintería", tipo = TipoPublicacion.SERVICIO
        ),
        Publicacion(
            id = "2", idNegocio = "1",
            titulo = "Mesa de madera artesanal",
            descripcion = "Mesa fabricada a mano con madera de pino, ideal para sala o comedor. Acabado barnizado.",
            precio = 450_000, categoria = "Carpintería", tipo = TipoPublicacion.PRODUCTO
        ),
        Publicacion(
            id = "3", idNegocio = "1",
            titulo = "Gabinete de cocina en roble",
            descripcion = "Gabinete con puertas batientes, acabado en roble y herrajes de acero inoxidable.",
            precio = 680_000, categoria = "Carpintería", tipo = TipoPublicacion.PRODUCTO
        ),

        // ── Negocio 2 ──────────────────────────────────────────────────────────
        Publicacion(
            id = "4", idNegocio = "2",
            titulo = "Sala comedor premium",
            descripcion = "Juego de sala y comedor de alta gama. Telas importadas y estructura en madera sólida.",
            precio = 4_800_000, categoria = "Hogar", tipo = TipoPublicacion.PRODUCTO
        ),
        Publicacion(
            id = "5", idNegocio = "2",
            titulo = "Estantería modular 5 niveles",
            descripcion = "Estantería ajustable y resistente, perfecta para cualquier espacio del hogar.",
            precio = 280_000, categoria = "Hogar", tipo = TipoPublicacion.PRODUCTO
        ),
        Publicacion(
            id = "6", idNegocio = "2",
            titulo = "Instalación y cableado eléctrico",
            descripcion = "Servicio profesional de instalación eléctrica residencial y comercial. Certificado incluido.",
            precio = 120_000, categoria = "Hogar", tipo = TipoPublicacion.SERVICIO
        ),

        // ── Negocio 3 ──────────────────────────────────────────────────────────
        Publicacion(
            id = "7", idNegocio = "3",
            titulo = "Laptop Asus ROG Strix G16",
            descripcion = "AMD Ryzen 9 · RTX 4060 · 16GB RAM · 512GB SSD. Pantalla 165Hz.",
            precio = 8_499_000, categoria = "Tecnología", tipo = TipoPublicacion.PRODUCTO
        ),
        Publicacion(
            id = "8", idNegocio = "3",
            titulo = "Teclado mecánico RGB",
            descripcion = "Teclado compacto 75% con switches táctiles y retroiluminación personalizable.",
            precio = 210_000, categoria = "Tecnología", tipo = TipoPublicacion.PRODUCTO
        ),
        Publicacion(
            id = "9", idNegocio = "3",
            titulo = "Mantenimiento y soporte técnico",
            descripcion = "Reparación de equipos, limpieza interna, actualización de software y hardware.",
            precio = 80_000, categoria = "Tecnología", tipo = TipoPublicacion.SERVICIO
        )
    )

    fun getPublicaciones(): List<Publicacion> = publicaciones

    fun getPublicacionById(id: String): Publicacion? =
        publicaciones.find { it.id == id }

    fun getPublicacionesByNegocio(idNegocio: String): List<Publicacion> =
        publicaciones.filter { it.idNegocio == idNegocio }

    fun getPublicacionesByTipo(tipo: TipoPublicacion): List<Publicacion> =
        publicaciones.filter { it.tipo == tipo }

    fun actualizarPublicacion(
        id: String,
        titulo: String,
        descripcion: String,
        precio: Long
    ) {
        val i = publicaciones.indexOfFirst { it.id == id }
        if (i != -1) {
            publicaciones[i] = publicaciones[i].copy(
                titulo = titulo,
                descripcion = descripcion,
                precio = precio
            )
        }
    }

    fun eliminarPublicacion(id: String) {
        publicaciones.removeIf { it.id == id }
    }
}