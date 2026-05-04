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

@Composable
fun ProfileScreen(navController: NavHostController) {
    val auth = Firebase.auth
    val usuario = auth.currentUser

    val nombre = usuario?.displayName?.ifEmpty { "Usuario" } ?: "Usuario"
    val correo = usuario?.email ?: "Sin correo"
    val telefono = usuario?.phoneNumber ?: "Sin teléfono"
    val inicial = nombre.firstOrNull()?.uppercaseChar()?.toString() ?: "U"

    var mostrarDialogo by remember { mutableStateOf(false) }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Cerrar sesión", fontWeight = FontWeight.Bold) },
            text  = { Text("¿Estás seguro que quieres cerrar sesión?") },
            confirmButton = {
                TextButton(onClick = {
                    auth.signOut()
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
                onHome   = { navController.navigate(Rutas.HOME) },
                onChats  = { navController.navigate(Rutas.MENSAJES) },
                onPerfil = { },
                onMenu   = { },
                onLogo   = { navController.navigate(Rutas.HOME) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE8E9EA))
                .padding(innerPadding)
        ) {
            // ── Encabezado
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
                    Text(inicial, fontSize = 32.sp,
                        fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(Modifier.height(10.dp))
                Text(nombre, fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold, color = Color.White)
                Text(correo, fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f))
                if (telefono != "Sin teléfono") {
                    Text(telefono, fontSize = 13.sp, color = Color.White.copy(alpha = 0.8f))
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Opciones
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column {
                        OpcionPerfil(texto = "Mi cuenta", icono = "👤")
                        HorizontalDivider(color = Color(0xFFEEEEEE))
                        OpcionPerfil(texto = "Mis negocios", icono = "🏪")
                        HorizontalDivider(color = Color(0xFFEEEEEE))
                        OpcionPerfil(texto = "Mis publicaciones", icono = "📋")
                        HorizontalDivider(color = Color(0xFFEEEEEE))
                        OpcionPerfil(texto = "Configuración", icono = "⚙️")
                    }
                }

                Spacer(Modifier.height(16.dp))

                // ── Botón cerrar sesión
                Button(
                    onClick = { mostrarDialogo = true },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFEBEE)
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text("Cerrar sesión", color = Color(0xFFE53935),
                        fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                }
            }
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
        Text(texto, fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f))
        Text("›", fontSize = 20.sp, color = Color.Gray)
    }
}
