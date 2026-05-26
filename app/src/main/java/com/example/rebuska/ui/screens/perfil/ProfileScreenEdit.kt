package com.example.rebuska.ui.screens.perfil

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rebuska.data.model.Negocio
import com.example.rebuska.data.repository.NegocioRepository
import com.example.rebuska.navigation.Rutas
import com.example.rebuska.ui.components.BottomNavBar
import com.example.rebuska.ui.components.NavDestino
import com.example.rebuska.ui.screens.publicacion.PublicacionDisplay
import com.example.rebuska.viewmodel.PublicacionViewModel
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@Composable
fun ProfileScreenEdit(navController: NavHostController, empresaId: String) {
    val viewModel: PublicacionViewModel = viewModel()

    val negocioState = remember { mutableStateOf<Negocio?>(null) }
    val cargando = remember { mutableStateOf(true) }

    LaunchedEffect(empresaId) {
        val result = NegocioRepository.getNegocioById(empresaId)
        result.onSuccess {
            negocioState.value = it
        }.onFailure {
            println("❌ Error al cargar negocio: ${it.message}")
        }
        cargando.value = false
    }

    LaunchedEffect(empresaId) {
        viewModel.cargarPublicaciones(empresaId)
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                seleccionado = NavDestino.PERFIL,
                onHome = { navController.navigate(Rutas.HOME) },
                onChats = { navController.navigate(Rutas.MENSAJES) },
                onPerfil = { navController.navigate(Rutas.PERFIL) },
                onMenu = { /* acción menú */ },
                onLogo = { navController.navigate(Rutas.HOME) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE8E9EA))
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            if (cargando.value) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1976D2))
                }
            } else {
                negocioState.value?.let { negocio ->
                    EmpresaEncabezado(
                        bannerUrl = negocio.bannerUrl,
                        onBackClick = { navController.popBackStack() },
                        onSettingsClick = { /* Acción ajustes */ }
                    )

                    SeccionPerfilNegocio(
                        nombre = negocio.nombre,
                        valoracion = negocio.promCalificacion.toDouble(),
                        reseñas = negocio.totalResenas,
                        logoUrl = negocio.logoUrl,
                        onEditPerfilClick = { /* Acción editar perfil */ }
                    )

                    //  edicion de descripcion
                    SeccionDescripcionEditable(
                        negocioId = negocio.id,
                        descripcion = negocio.descripcion ?: "Sin descripción disponible"
                    )

                } ?: run {
                    Text(
                        text = "No se encontró información del negocio.",
                        modifier = Modifier.padding(16.dp),
                        color = Color.Gray
                    )
                }
            }

            SeccionMisPublicaciones(
                publicaciones = viewModel.publicaciones.map { publicacion ->
                    PublicacionDisplay(
                        id = publicacion.id,
                        titulo = publicacion.titulo,
                        tiempoPublicacion = "reciente",
                        descripcion = publicacion.descripcion,
                        imagenUrl = publicacion.fotoUrl
                    )
                },
                onNuevaPublicacionClick = {
                    navController.navigate(Rutas.crearPublicacionRuta(empresaId))
                },
                onEditPublicacionClick = { publicacion ->
                    navController.navigate("editarPublicacion/${publicacion.id}")
                },
                onDeletePublicacionClick = { publicacion ->
                    viewModel.eliminarPublicacion(publicacion.id)
                }
            )
        }
    }
}

@Composable
fun EmpresaEncabezado(
    bannerUrl: String?,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        // Fondo con banner del negocio
        bannerUrl?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Banner del negocio",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1976D2))
        )

        // Botón atrás
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0x33000000))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Atrás",
                tint = Color.White
            )
        }

        // Botón ajustes
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0x33000000))
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Ajustes",
                tint = Color.White
            )
        }
    }
}

@Composable
fun SeccionPerfilNegocio(
    nombre: String,
    valoracion: Double,
    reseñas: Int,
    logoUrl: String?,
    onEditPerfilClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // ───────── Fila principal: logo + información ─────────
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Foto de perfil (logo)
            logoUrl?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Logo del negocio",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape)
                )
            } ?: Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEEEEE)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    nombre.firstOrNull()?.uppercase() ?: "N",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información del negocio (nombre, reseñas, botón)
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = nombre,
                    color = Color.Black,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFE8E9EA))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Valoración",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "$valoracion",
                        color = Color.Black,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    Text(
                        text = "· $reseñas reseñas",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onEditPerfilClick,
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                    elevation = ButtonDefaults.buttonElevation(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar perfil",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Editar perfil",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SeccionDescripcionEditable(
    negocioId: String,
    descripcion: String
) {
    var mostrarDialogo by remember { mutableStateOf(false) }
    var nuevaDescripcion by remember { mutableStateOf(descripcion) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Canvas(modifier = Modifier.size(8.dp)) {
                        drawCircle(color = Color(0xFF1976D2))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Descripción",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                TextButton(onClick = { mostrarDialogo = true }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF1976D2)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Editar",
                        color = Color(0xFF1976D2)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = descripcion.ifEmpty { "Sin descripción disponible" },
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color.DarkGray
            )
        }
    }

    // ───────── Diálogo para editar ─────────
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Editar descripción") },
            text = {
                TextField(
                    value = nuevaDescripcion,
                    onValueChange = { nuevaDescripcion = it },
                    label = { Text("Nueva descripción") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    NegocioRepository.actualizarDescripcionNegocio(
                        negocioId,
                        nuevaDescripcion
                    )
                    mostrarDialogo = false
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun SeccionMisPublicaciones(
    publicaciones: List<PublicacionDisplay>,
    onNuevaPublicacionClick: () -> Unit,
    onEditPublicacionClick: (PublicacionDisplay) -> Unit,
    onDeletePublicacionClick: (PublicacionDisplay) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Encabezado con botón "Nueva"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Canvas(modifier = Modifier.size(8.dp)) {
                    drawCircle(color = Color(0xFF1976D2))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Mis publicaciones",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Button(
                onClick = onNuevaPublicacionClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                shape = RoundedCornerShape(50)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Nueva", tint = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Nueva", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de publicaciones
        publicaciones.forEach { publicacion ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Imagen desde Firebase Storage
                    Image(
                        painter = rememberAsyncImagePainter(publicacion.imagenUrl),
                        contentDescription = "Imagen de la publicación",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Texto de la publicación
                    Column(modifier = Modifier.weight(1f)) {
                        Text(publicacion.titulo, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Text("Publicado hace ${publicacion.tiempoPublicacion}", color = Color.Gray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = publicacion.descripcion,
                            fontSize = 14.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Botones de acción
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Botón editar
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2196F3))
                                .clickable { onEditPublicacionClick(publicacion) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        // Botón eliminar
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF44336))
                                .clickable { onDeletePublicacionClick(publicacion) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenEditPreview() {
    val navController = rememberNavController()
    ProfileScreenEdit(navController, empresaId = "1")
}