package com.example.rebuska.ui.screens.publicacion

import android.net.Uri
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.rebuska.viewmodel.PublicacionViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicacionFormScreen(
    idNegocio: String,
    onBack: () -> Unit,
    viewModel: PublicacionViewModel = viewModel()
) {
    val context = LocalContext.current
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("SERVICIO") } // valor inicial
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var mensaje by remember { mutableStateOf<String?>(null) }

    val imagenLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { imagenUri = it }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Nueva publicación", color = Color.White) },
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
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título de la publicación") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción del servicio o producto") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            // 🔹 Selector de tipo (Servicio o Producto)
            Text("Tipo de publicación", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterChip(
                    selected = tipo == "SERVICIO",
                    onClick = { tipo = "SERVICIO" },
                    label = { Text("Servicio") },
                    leadingIcon = {
                        if (tipo == "SERVICIO") {
                            Icon(Icons.Default.Check, contentDescription = null)
                        }
                    }
                )
                FilterChip(
                    selected = tipo == "PRODUCTO",
                    onClick = { tipo = "PRODUCTO" },
                    label = { Text("Producto") },
                    leadingIcon = {
                        if (tipo == "PRODUCTO") {
                            Icon(Icons.Default.Check, contentDescription = null)
                        }
                    }
                )
            }

            Spacer(Modifier.height(20.dp))

            // Vista previa de imagen seleccionada
            imagenUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
                Spacer(Modifier.height(12.dp))
            }

            Button(
                onClick = { imagenLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Seleccionar imagen", color = Color.White)
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.crearPublicacion(
                        titulo = titulo,
                        descripcion = descripcion,
                        precio = precio.toLongOrNull() ?: 0L,
                        tipo = tipo, // ✅ se guarda el tipo seleccionado
                        idNegocio = idNegocio,
                        imagenUri = imagenUri
                    )
                    mensaje = "Publicación creada correctamente ✅"
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Guardar publicación", color = Color.White, fontWeight = FontWeight.Bold)
            }

            mensaje?.let {
                Spacer(Modifier.height(16.dp))
                Text(
                    it,
                    color = if (it.contains("Error")) Color.Red else Color(0xFF388E3C),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
