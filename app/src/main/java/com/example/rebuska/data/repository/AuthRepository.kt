package com.example.rebuska.data.repository

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

object AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db   = FirebaseFirestore.getInstance()

    // ══════════════════════════════════════════════════════
    // REGISTRO + FIRESTORE
    // ══════════════════════════════════════════════════════

    suspend fun registrarUsuario(
        email: String,
        password: String,
        datosUsuario: Map<String, Any>
    ): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid    = result.user!!.uid

            // ── Actualizar displayName con nombre + apellido ──
            val nombre   = datosUsuario["nombre"]   as? String ?: ""
            val apellido = datosUsuario["apellido"] as? String ?: ""
            val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                .setDisplayName("$nombre $apellido".trim())
                .build()
            result.user?.updateProfile(profileUpdates)?.await()

            // Enviar verificación de correo
            result.user?.sendEmailVerification()?.await()

            // Guardar datos en Firestore
            db.collection("usuarios").document(uid).set(datosUsuario).await()

            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ══════════════════════════════════════════════════════
    // LOGIN
    // ══════════════════════════════════════════════════════

    suspend fun iniciarSesion(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ══════════════════════════════════════════════════════
    // VERIFICACIÓN DE EMAIL
    // ══════════════════════════════════════════════════════

    suspend fun enviarVerificacionEmail(
        email: String,
        password: String
    ): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.sendEmailVerification()?.await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verificarEmail(): Result<Boolean> {
        return try {
            auth.currentUser?.reload()?.await()
            val verificado = auth.currentUser?.isEmailVerified ?: false
            Result.success(verificado)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun reenviarVerificacionEmail(): Result<Unit> {
        return try {
            auth.currentUser?.sendEmailVerification()?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ══════════════════════════════════════════════════════
    // VERIFICACIÓN POR TELÉFONO / SMS
    // ══════════════════════════════════════════════════════

    fun iniciarVerificacionTelefono(
        telefono: String,
        activity: Activity,
        onCodeSent: (verificationId: String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(telefono)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    auth.signInWithCredential(credential)
                }

                override fun onVerificationFailed(e: com.google.firebase.FirebaseException) {
                    onError(e)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    onCodeSent(verificationId)
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    suspend fun verificarCodigoSMS(
        verificationId: String,
        codigo: String
    ): Result<FirebaseUser> {
        return try {
            val credential = PhoneAuthProvider.getCredential(verificationId, codigo)
            val result     = auth.signInWithCredential(credential).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ══════════════════════════════════════════════════════
    // UTILIDADES
    // ══════════════════════════════════════════════════════

    fun getUsuarioActual(): FirebaseUser? = auth.currentUser

    fun cerrarSesion() = auth.signOut()

    fun traducirError(msg: String?): String = when {
        msg == null                                  -> "Error desconocido"
        "email address is already in use"   in msg   -> "Este correo ya está registrado"
        "password is invalid"               in msg   -> "Contraseña incorrecta"
        "no user record"                    in msg   -> "No existe una cuenta con este correo"
        "badly formatted"                   in msg   -> "El correo no tiene un formato válido"
        "network error"                     in msg   -> "Sin conexión a internet"
        "weak-password"                     in msg   -> "La contraseña debe tener al menos 6 caracteres"
        else                                         -> "Error: $msg"
    }
}