package com.example.rebuska.ui.screens.negocio

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.rebuska.data.model.Negocio
import com.example.rebuska.ui.viewmodel.NegocioViewModel

@Composable
fun NegocioFormScreen(viewModel: NegocioViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var logoBytes by remember { mutableStateOf<ByteArray?>(null) }
    var bannerBytes by remember { mutableStateOf<ByteArray?>(null) }

    val logoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            logoBytes = context.contentResolver.openInputStream(it)?.use { stream -> stream.readBytes() }
        }
    }

    val bannerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            bannerBytes = context.contentResolver.openInputStream(it)?.use { stream -> stream.readBytes() }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        TextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
        TextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") })

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { logoLauncher.launch("image/*") }) { Text("Seleccionar logo") }
        Button(onClick = { bannerLauncher.launch("image/*") }) { Text("Seleccionar banner") }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val negocio = Negocio(
                nombre = nombre,
                descripcion = descripcion,
                categoria = categoria
            )
            viewModel.crearNegocio(negocio, logoBytes, bannerBytes)
            onBack()
        }) {
            Text("Guardar negocio")
        }
    }
}
