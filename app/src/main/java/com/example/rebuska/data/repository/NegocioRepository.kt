package com.example.rebuska.data.repository

import com.example.rebuska.data.model.Negocio
import com.example.rebuska.data.remote.FirestoreService

object NegocioRepository {

    suspend fun getNegocios(): Result<List<Negocio>> =
        FirestoreService.getNegocios()

    suspend fun getNegocioById(id: String): Result<Negocio> =
        FirestoreService.getNegocioById(id)

    suspend fun getNegociosByTrabajador(idTrabajador: String): Result<List<Negocio>> =
        FirestoreService.getNegociosByTrabajador(idTrabajador)

    suspend fun crearNegocio(negocio: Negocio): Result<String> =
        FirestoreService.crearNegocio(negocio)

    suspend fun actualizarNegocio(id: String, campos: Map<String, Any>): Result<Unit> =
        FirestoreService.actualizarNegocio(id, campos)

    suspend fun eliminarNegocio(id: String): Result<Unit> =
        FirestoreService.eliminarNegocio(id)
}