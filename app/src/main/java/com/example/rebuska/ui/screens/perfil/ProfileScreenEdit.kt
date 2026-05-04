package com.example.rebuska.ui.screens.perfil

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.navigation.NavController
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rebuska.R
import com.example.rebuska.navigation.Rutas
import com.example.rebuska.ui.components.BottomNavBar
import com.example.rebuska.ui.components.NavDestino

// data class para vista (provisional)
data class EmpresaDisplay(
    val nombre: String,
    val valoracion: Double,
    val resenas: Int,
    val logo: Int
)

data class PublicacionDisplay(
    val id: Int,
    val titulo: String,
    val tiempoPublicacion: String,
    val descripcion: String,
    val imagen: Int
)

@Composable
fun ProfileScreenEdit(navController: NavHostController, empresaId: Int) {
    // datos de la empresa
    val empresaInfo = EmpresaDisplay(
        nombre = "Carpintería López",
        valoracion = 4.7,
        resenas = 128,
        logo = R.drawable.logo_carpinteria
    )

    val publicacionesLista = remember { mutableStateListOf(
        PublicacionDisplay(
            id = 1,
            titulo = "Muebles a medida",
            tiempoPublicacion = "2 días",
            descripcion = "Diseño y fabricación de muebles personalizados para sala, comedor y alcoba. Madera de primera calidad con acabados finos.",
            imagen = R.drawable.muebles_medida
        ),
        PublicacionDisplay(
            id = 2,
            titulo = "Instalación de puertas y ventanas",
            tiempoPublicacion = "1 semana",
            descripcion = "Instalación profesional de puertas y ventanas de madera. Incluye ajuste y garantía de 6 meses.",
            imagen = R.drawable.instalacion_puerta
        )
    )}
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
                .verticalScroll(rememberScrollState()) // para scroll si el contenido es muy amplio
        ) {
            // encabezado especifico para la pantalla de edicion de empresa
            EmpresaEncabezado(
                nombre = empresaInfo.nombre,
                valoracion = empresaInfo.valoracion,
                reseñas = empresaInfo.resenas,
                imagenRes = empresaInfo.logo,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = { /* Acción de ajustes */ }
            )
            Spacer(modifier = Modifier.height(16.dp))
            //dexripcion
            SeccionDescripcion(
                    descripcion = "Carpintería local con más de 20 años de experiencia, comprometidos en ofrecer los mejores productos de madera. Expertos en muebles a medida, puertas, ventanas y restauración.",
                    onEditClick = { /* Acción para editar descripción */ }
                )

                Spacer(modifier = Modifier.height(8.dp))

                //seccion de publicaciones
                SeccionMisPublicaciones(
                    navController = navController,
                    publicaciones = publicacionesLista,
                    onNuevaPublicacionClick = { /* acción nueva */ },
                    onEditPublicacionClick = { /* acción editar */ },
                    onDeletePublicacionClick = { /* acción eliminar */ }
                        // aqui iria la logica para la base de datos

                )
            }
        }
    }


// componentes internnos

@Composable
fun EmpresaEncabezado(
    nombre: String,
    valoracion: Double,
    reseñas: Int,
    imagenRes: Int,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(Color(0xFF1976D2))
    ) {
        // titulo
        Text(
            text = "Mi empresa",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )

        // boton atras
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 16.dp)
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0x33FFFFFF))
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Atrás",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        // boton ajustes
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 16.dp)
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0x33FFFFFF))
                .clickable { onSettingsClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Ajustes",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        // contenido central
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imagenRes),
                contentDescription = "Logo de la empresa",
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = nombre,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x33FFFFFF))
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
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Text(
                    text = "· $reseñas reseñas",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun SeccionDescripcion(
    descripcion: String,
    onEditClick: () -> Unit
) {
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
                        drawCircle(color = Color(0xFF1976D2)) // Punto azul
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Descripción",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                TextButton(onClick = onEditClick) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
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
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = descripcion,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun SeccionMisPublicaciones(
    navController: NavController, // ← agrégalo aquí
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
                onClick = { navController.navigate("negocioForm") }, // ✅ ahora sí existe
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                shape = RoundedCornerShape(50)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Nueva", tint = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Nueva", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

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
                    Image(
                        painter = painterResource(id = publicacion.imagen),
                        contentDescription = "Imagen de la publicación",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = publicacion.titulo,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Publicado hace ${publicacion.tiempoPublicacion}",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = publicacion.descripcion,
                            fontSize = 14.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // icono de editar
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2196F3))
                                .clickable { onEditPublicacionClick(publicacion) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Editar publicación",
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        // icono de eliminar
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF44336))
                                .clickable { onDeletePublicacionClick(publicacion) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Eliminar publicación",
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
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
    ProfileScreenEdit(navController, empresaId = 1)
}