package com.example.rebuska.data.remote

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage // <--- ¡Asegúrate de tener esta importación!
import kotlinx.coroutines.tasks.await
import android.util.Log // Importa Log para depuración


object StorageService { // O si lo inyectas, puede ser una clase
    private val storage = FirebaseStorage.getInstance()

    suspend fun uploadImage(bytes: ByteArray, path: String): Result<String> = runCatching {
        Log.d("StorageService", "uploadImage: Iniciando subida de imagen a la ruta: $path")
        val ref = storage.reference.child(path)
        ref.putBytes(bytes).await()
        val downloadUrl = ref.downloadUrl.await().toString()
        Log.d("StorageService", "uploadImage: Imagen subida exitosamente. URL: $downloadUrl")
        downloadUrl
    }
}