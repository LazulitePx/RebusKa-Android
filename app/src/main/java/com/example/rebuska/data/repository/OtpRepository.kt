package com.example.rebuska.data.repository

import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await

object OtpRepository {

    private val functions = FirebaseFunctions.getInstance("us-central1")


    suspend fun enviarOtp(
        email: String
    ): Result<Unit> {

        return try {

            val data = hashMapOf(
                "email" to email
            )

            functions
                .getHttpsCallable("enviarOtpCorreo")
                .call(data)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            android.util.Log.e("OTP", "Error enviando OTP: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun verificarOtp(
        email: String,
        codigo: String
    ): Result<Boolean> {

        return try {

            val data = hashMapOf(
                "email" to email,
                "codigo" to codigo
            )

            functions
                .getHttpsCallable("verificarOtpCorreo")
                .call(data)
                .await()

            Result.success(true)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}