package com.example.rebuska.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rebuska.navigation.Rutas
import com.example.rebuska.ui.components.BottomNavBar
import com.example.rebuska.ui.components.NavDestino
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import androidx.compose.material.icons.Icons
import com.example.rebuska.data.model.Negocio
import com.example.rebuska.data.repository.NegocioRepository
import coil.compose.AsyncImage
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.painterResource
import com.example.rebuska.R


@Composable
fun ProfileScreen(navController: NavHostController) {
    val auth = Firebase.auth
    var usuario by remember { mutableStateOf(auth.currentUser) }
    var tipoUsuario by remember { mutableStateOf<String?>(null) }
    var negocios by remember { mutableStateOf<List<Negocio>>(emptyList()) }

    var nombre by remember { mutableStateOf("Usuario") }
    var correo by remember { mutableStateOf("") }
    val inicial by remember(nombre) { derivedStateOf { nombre.firstOrNull()?.uppercaseChar()?.toString() ?: "U" } }

    // Cargar datos del usuario y su tipo (trabajador o cliente)
    LaunchedEffect(usuario) {
        usuario?.let { user ->
            correo = user.email ?: user.phoneNumber ?: "Sin información"
            try {
                val doc = com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    .collection("usuarios")
                    .document(user.uid)
                    .get()
                    .await()

                android.util.Log.d("PERFIL", "UID buscado: ${user.uid}")
                android.util.Log.d("PERFIL", "doc.exists(): ${doc.exists()}")
                android.util.Log.d("PERFIL", "doc.data: ${doc.data}")

                nombre      = doc.getString("nombre") ?: user.displayName ?: "Usuario"
                tipoUsuario = doc.getString("tipo")?.trim()?.lowercase()

                android.util.Log.d("PERFIL", "tipo cargado: $tipoUsuario")

                if (tipoUsuario == "trabajador") {
                    val result = NegocioRepository.getNegociosByTrabajador(user.uid)
                    result.onSuccess { lista -> negocios = lista }
                        .onFailure { e -> android.util.Log.e("PERFIL", "Error negocios: ${e.message}") }
                }

            } catch (e: Exception) {
                android.util.Log.e("PERFIL", "Error cargando perfil: ${e.message}", e)
                tipoUsuario = "cliente" // fallback para no quedar en spinner infinito
            }
        }
    }

    var mostrarDialogo by remember { mutableStateOf(false) }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Cerrar sesión", fontWeight = FontWeight.Bold) },
            text = { Text("¿Estás seguro que quieres cerrar sesión?") },
            confirmButton = {
                TextButton(onClick = {
                    auth.signOut()
                    usuario = null
                    tipoUsuario = null
                    mostrarDialogo = false
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.HOME) { inclusive = true }
                    }
                }) {
                    Text("Sí, salir", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                seleccionado = NavDestino.PERFIL,
                onHome = { navController.navigate(Rutas.HOME) },
                onChats = { navController.navigate(Rutas.MENSAJES) },
                onPerfil = { },
                onMenu = { },
                onLogo = { navController.navigate(Rutas.HOME) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE8E9EA))
                .padding(innerPadding)
        ) {
            // ── Encabezado ──
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF1e5dba), Color(0xFF2989e0))
                        )
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(inicial, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(Modifier.height(10.dp))
                Text(nombre, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                Text(correo, fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f))
            }

            Spacer(Modifier.height(16.dp))

            when {
                usuario == null -> {
                    VistaNoLogueado(navController)
                }

                tipoUsuario == null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                    }
                }

                tipoUsuario == "cliente" -> {
                    VistaCliente(navController, onCerrarSesion = { mostrarDialogo = true })
                }

                tipoUsuario == "trabajador" -> {
                    VistaTrabajador(navController, negocios, onCerrarSesion = { mostrarDialogo = true })
                }
            }
        }
    }
}


@Composable
fun VistaNoLogueado(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Únete a nosotros", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1e5dba))
        Spacer(Modifier.height(8.dp))
        Text(
            "Crea una cuenta y empieza a ofrecer tus servicios o contratar profesionales.",
            fontSize = 15.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 32.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { navController.navigate(Rutas.LOGIN) },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text("Iniciar sesión", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(
            onClick = { navController.navigate(Rutas.REGISTRO_ROL) },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1976D2))
        ) {
            Text("Crear cuenta", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun VistaCliente(navController: NavHostController, onCerrarSesion: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column {
                OpcionPerfil("Mi cuenta", "👤")
                HorizontalDivider(color = Color(0xFFEEEEEE))
                OpcionPerfil("Historial", "🕓")
                HorizontalDivider(color = Color(0xFFEEEEEE))
                OpcionPerfil("Configuración", "⚙️")
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text("Cerrar sesión", color = Color(0xFFE53935), fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
        }
    }
}

@Composable
fun VistaTrabajador(
    navController: NavHostController,
    negocios: List<Negocio>,
    onCerrarSesion: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp).verticalScroll(rememberScrollState())) {
        Text(
            "Mis negocios",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1e5dba)
        )
        Spacer(Modifier.height(8.dp))

        // 🔹 Límite de creación de negocios
        val puedeCrear = negocios.size < 2

        Button(
            onClick = {
                if (puedeCrear) {
                    navController.navigate("negocioForm")
                }
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (puedeCrear) Color(0xFF1976D2) else Color.Gray
            )
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Nueva", tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text(
                if (puedeCrear) "Nueva" else "Límite alcanzado",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        // Lista de negocios
        if (negocios.isEmpty()) {
            Text(
                "Aún no tienes negocios creados.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(8.dp)
            )
        } else {
            negocios.forEach { negocio ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            navController.navigate(Rutas.negocioRuta(negocio.id))
                        },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!negocio.logoUrl.isNullOrEmpty()) {
                            AsyncImage(
                                model = negocio.logoUrl,
                                contentDescription = negocio.nombre,
                                modifier = Modifier.size(48.dp).clip(CircleShape)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEEEEEE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    negocio.nombre.firstOrNull()?.uppercase() ?: "N",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1976D2)
                                )
                            }
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(negocio.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(negocio.categoria, fontSize = 13.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        //  Banner de membresía Rebuska Pro (solo si tiene 2 negocios)
        if (negocios.size >= 2) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1e5dba)),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_visajoso),
                        contentDescription = "Rebuska Pro",
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "¿Tienes más de un negocio?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Text(
                            "Con Rebuska Pro gestionas múltiples empresas y destacas tus publicaciones.",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    Button(
                        onClick = { /* Acción para ver planes */ },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text("Ver planes", color = Color(0xFF1e5dba), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Botón cerrar sesión
        Button(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text(
                "Cerrar sesión",
                color = Color(0xFFE53935),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun OpcionPerfil(texto: String, icono: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icono, fontSize = 20.sp)
        Spacer(Modifier.width(12.dp))
        Text(
            texto,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
        Text("›", fontSize = 20.sp, color = Color.Gray)
    }
}

