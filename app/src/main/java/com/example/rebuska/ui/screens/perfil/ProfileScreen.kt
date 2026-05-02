package com.example.rebuska.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import com.example.rebuska.navigation.Rutas
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.rebuska.ui.components.BottomNavBar
import com.example.rebuska.ui.components.NavDestino
import com.example.rebuska.R


@Composable
fun ProfileScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                seleccionado = NavDestino.PERFIL,
                onHome   = { navController.navigate(Rutas.HOME) },
                onChats  = { navController.navigate(Rutas.MENSAJES) },
                onPerfil = { navController.navigate(Rutas.PERFIL) },
                onMenu   = { /* acción menú */ },
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
            // encabezado para reutilizar
            Encabezado(
                nombre = "Carlos López",
                correo = "carlos.lopez@gmail.com",
                valoracion = 4.7,
                reseñas = 128,
                imagenRes = R.drawable.foto_perfil_trabajador
            )

            Spacer(modifier = Modifier.height(16.dp))

            //seccion de mas negocios
            MisNegociosSection(
                onNuevoNegocio = {},
                onVerNegocio = {id ->
                    navController.navigate(Rutas.negocioRuta(id))
                               },
                onVerPlanes = {
                    // TODO: acción para ver planes de Rebuska Pro
                    // mas adelante porne algo sobre los planes
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MisNegociosSection(
    onNuevoNegocio: () -> Unit,
    onVerNegocio: (String) -> Unit,
    onVerPlanes: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        // encabezado de la seccion
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2196F3))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Mis Negocios", style = MaterialTheme.typography.titleMedium)
            }

            // boton con degradado igual al encabezado
            Button(
                onClick = onNuevoNegocio,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFF1e5dba), Color(0xFF2989e0))
                            ),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = "+ Nueva", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tarjeta de negocio
       NegocioCard(
            nombre = "Carpintería López",
            rating = 4.7,
            publicacionesActivas = 5,
            onClick = { onVerNegocio("1") }

        )
        // añadir mas negocioCard aqui si el usuario tiene varios negocios

        Spacer(modifier = Modifier.height(8.dp))

        // Tarjeta promocional
        PromoRebuskaPro(onVerPlanes = onVerPlanes)
    }
}

@Composable
fun Encabezado(
    nombre: String,
    correo: String,
    valoracion: Double,
    reseñas: Int,
    imagenRes: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1e5dba), Color(0xFF2989e0))
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp).clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imagenRes),
                contentDescription = "Imagen de encabezado",
                modifier = Modifier.size(80.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            //iniciales si no hay foto
            Text(text = "CL", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = nombre, style = MaterialTheme.typography.titleLarge, color = Color.White)
        Text(text = correo, style = MaterialTheme.typography.bodyMedium, color = Color.LightGray)

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "⭐ $valoracion - $reseñas reseñas",
            color = Color.Yellow,
            style = MaterialTheme.typography.bodyMedium)
    }
}

// Tarjeta individual para mostrar un negocio en ProfileScreen
@Composable
fun NegocioCard(
    nombre: String,
    rating: Double,
    publicacionesActivas: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // logo del negocio
            Image(
                painter = painterResource(id = R.drawable.logo_carpinteria),
                contentDescription = "Logo Carpintería López",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(text = nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = "⭐ $rating")
                Text(text = "$publicacionesActivas publicaciones activas")
            }
        }
    }
}
//Tarjeta promocial
@Composable
fun PromoRebuskaPro(onVerPlanes: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2989e0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_visajoso),
                contentDescription = "Logo Rebuska",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1e5dba)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "¿Tienes más de un negocio?",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Con Rebuska Pro gestionas múltiples negocios y destacas tus publicaciones.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onVerPlanes,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(text = "Ver planes", color = Color(0xFF2989e0))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    ProfileScreen(navController)
}
