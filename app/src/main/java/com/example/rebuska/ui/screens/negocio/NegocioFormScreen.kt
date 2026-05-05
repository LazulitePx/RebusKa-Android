package com.example.rebuska.ui.screens.negocio

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.rebuska.ui.viewmodel.NegocioViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NegocioFormScreen(viewModel: NegocioViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var logoBytes by remember { mutableStateOf<ByteArray?>(null) }
    var bannerBytes by remember { mutableStateOf<ByteArray?>(null) }
    var logoUri by remember { mutableStateOf<String?>(null) }
    var bannerUri by remember { mutableStateOf<String?>(null) }
    var mensaje by remember { mutableStateOf<String?>(null) }

    // lanzadores para seleccionar imagenes desde galeria
    val logoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            logoBytes = context.contentResolver.openInputStream(it)?.use { stream -> stream.readBytes() }
            logoUri = it.toString()
        }
    }

    val bannerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            bannerBytes = context.contentResolver.openInputStream(it)?.use { stream -> stream.readBytes() }
            bannerUri = it.toString()
        }
    }

    // visual del formulario
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Nuevo negocio", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF1976D2))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(16.dp))

            // vista previa del logo
            logoUri?.let { Image(painter = rememberAsyncImagePainter(it), contentDescription = "Logo", modifier = Modifier.size(100.dp)) }
            Button(onClick = { logoLauncher.launch("image/*") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(50.dp)) {
                Text("Seleccionar logo", color = Color.White)
            }
            Spacer(Modifier.height(12.dp))

            // vista previa del banner
            bannerUri?.let { Image(painter = rememberAsyncImagePainter(it), contentDescription = "Banner", modifier = Modifier.fillMaxWidth().height(120.dp)) }
            Button(onClick = { bannerLauncher.launch("image/*") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(50.dp)) {
                Text("Seleccionar banner", color = Color.White)
            }

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = {
                    viewModel.crearNegocio(
                        nombre = nombre,
                        descripcion = descripcion,
                        categoria = categoria,
                        logoBytes = logoBytes,
                        bannerBytes = bannerBytes,
                        onSuccess = {
                            mensaje = "Negocio guardado correctamente ✅"
                            onBack()
                        },
                        onError = { error ->
                            mensaje = "Error: $error"
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Guardar negocio", color = Color.White, fontWeight = FontWeight.Bold)
            }

            mensaje?.let {
                Spacer(Modifier.height(16.dp))
                Text(it, color = if (it.contains("Error")) Color.Red else Color(0xFF388E3C), fontWeight = FontWeight.Bold)
            }
        }
    }
}

