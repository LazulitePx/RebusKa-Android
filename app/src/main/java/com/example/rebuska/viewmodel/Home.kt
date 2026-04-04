package com.example.rebuska.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rebuska.R
import com.example.rebuska.data.model.Publicacion
import com.example.rebuska.data.model.TipoPublicacion
import com.example.rebuska.data.repository.NegocioRepository
import com.example.rebuska.data.repository.PublicacionRepository
import com.example.rebuska.ui.components.BottomNavBar
import com.example.rebuska.ui.components.NavDestino
import com.example.rebuska.ui.theme.*

data class Categoria(val emoji: String, val nombre: String)

val categorias = listOf(
    Categoria("🔧", "Mantenimiento"),
    Categoria("🏠", "Hogar"),
    Categoria("🚗", "Automotriz"),
    Categoria("💻", "Tecnología"),
    Categoria("🪑", "Carpintería"),
    Categoria("🌿", "Jardín"),
)

@Composable
fun HomeScreen(
    onVerTienda: (idNegocio: Int) -> Unit = {},
    onVerCategorias: () -> Unit = {},
    onPerfil: () -> Unit = {},
    onChats: () -> Unit = {},
    onLogin: () -> Unit = {}
) {
    var busqueda by remember { mutableStateOf("") }
    val negocios = remember { NegocioRepository.getNegocios() }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                seleccionado = NavDestino.HOME,
                onHome   = {},
                onChats  = onChats,   // 🔥 importante
                onLogo   = onLogin,
                onPerfil = onPerfil,
                onMenu   = {}
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF2F4F8))
                .padding(innerPadding)
        ) {

            // ── Header azul  ──────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(Blue800, Blue700, Blue400),
                                androidx.compose.ui.geometry.Offset(0f, 0f),
                                androidx.compose.ui.geometry.Offset(400f, 300f)
                            )
                        )

                        .padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Buscador
                        OutlinedTextField(
                            value = busqueda,
                            onValueChange = { busqueda = it },
                            placeholder = {
                                Text(
                                    "Buscar producto o servicio...",
                                    fontFamily = Nunito, fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp, color = Color(0xFFAAAAAA)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    painterResource(R.drawable.ic_search), null,
                                    tint = Color(0xFFAAAAAA), modifier = Modifier.size(18.dp)
                                )
                            },
                            singleLine = true,
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(50.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor   = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor   = Color.White,
                                unfocusedContainerColor = Color.White,
                                cursorColor = Blue800
                            )
                        )
                        // Campana de notificaciones
                        Box(contentAlignment = Alignment.TopEnd) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.18f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painterResource(R.drawable.ic_notifications), null,
                                    tint = Color.White, modifier = Modifier.size(20.dp)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE53935))
                                    .offset(x = 2.dp, y = (-2).dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("3", fontFamily = Nunito, fontWeight = FontWeight.ExtraBold,
                                    fontSize = 8.sp, color = Color.White)
                            }
                        }
                    }
                }
            }

            // ── Banner promocional ────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 14.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFFF8E27))
                        .height(150.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "¡La solución\na tu alcance!",
                                fontFamily = Nunito, fontWeight = FontWeight.Black,
                                fontSize = 20.sp, color = Color.White, lineHeight = 25.sp
                            )
                            Spacer(Modifier.height(12.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Blue800)
                                    .padding(horizontal = 16.dp, vertical = 7.dp)
                            ) {
                                Text("Buscar ofertas", fontFamily = Nunito,
                                    fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = Color.White)
                            }
                        }
                        Image(
                            painter = painterResource(R.drawable.ic_banner),
                            contentDescription = "Trabajador",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.width(195.dp).fillMaxHeight()
                        )
                    }
                }
            }

            // ── Categorías ────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Categorías", fontFamily = Nunito, fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp, color = TextPrimary)
                        TextButton(onClick = onVerCategorias, contentPadding = PaddingValues(0.dp)) {
                            Text("Ver todas >", fontFamily = Nunito, fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp, color = Blue800)
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(categorias) { cat ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(68.dp)) {
                                Box(
                                    modifier = Modifier.size(56.dp).clip(RoundedCornerShape(16.dp)).background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) { Text(cat.emoji, fontSize = 24.sp) }
                                Spacer(Modifier.height(6.dp))
                                Text(cat.nombre, fontFamily = Nunito, fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp, color = TextPrimary, maxLines = 1)
                            }
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                }
            }

            // ── Título ofertas ────────────────────────
            item {
                Text("Ofertas disponibles", fontFamily = Nunito, fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp, color = TextPrimary, modifier = Modifier.padding(horizontal = 14.dp))
                Spacer(Modifier.height(12.dp))
            }

            // ── 1 card por negocio ────────────────────
            items(negocios) { negocio ->
                val primeraPublicacion = PublicacionRepository
                    .getPublicacionesByNegocio(negocio.id)
                    .firstOrNull()

                if (primeraPublicacion != null) {
                    PublicacionCardHome(
                        publicacion      = primeraPublicacion,
                        negocioNombre    = negocio.nombre,
                        negocioCategoria = negocio.categoria,
                        banner           = negocio.banner,
                        esPopular        = negocio.id == 1,
                        onClick          = { onVerTienda(negocio.id) }
                    )
                }
            }

            item { Spacer(Modifier.height(12.dp)) }
        }
    }
}

// ── Card de negocio en el Home ────────────────────────

@Composable
fun PublicacionCardHome(
    publicacion: Publicacion,
    negocioNombre: String,
    negocioCategoria: String,
    banner: Int?,
    esPopular: Boolean = false,
    onClick: () -> Unit = {}
) {
    val bannerColors = when (negocioNombre) {
        "Carpintería López" -> listOf(Color(0xFF5D4037), Color(0xFF8D6E63))
        "Muebles Alta"      -> listOf(Color(0xFF37474F), Color(0xFF78909C))
        "CompuTech"         -> listOf(Color(0xFF0D0D1A), Color(0xFF0A3D62))
        else                -> listOf(Blue800, Blue700)
    }
    val bannerEmoji = when (negocioNombre) {
        "Carpintería López" -> "🪚"
        "Muebles Alta"      -> "🛋️"
        "CompuTech"         -> "🖥️"
        else                -> "📦"
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(155.dp)) {
                if (banner != null) {
                    Image(
                        painter = painterResource(banner),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize().background(
                            Brush.linearGradient(bannerColors,
                                androidx.compose.ui.geometry.Offset(0f, 0f),
                                androidx.compose.ui.geometry.Offset(600f, 300f))
                        ),
                        contentAlignment = Alignment.Center
                    ) { Text(bannerEmoji, fontSize = 60.sp) }
                }
                if (esPopular) {
                    Box(
                        modifier = Modifier.align(Alignment.TopEnd).padding(10.dp)
                            .clip(RoundedCornerShape(20.dp)).background(Color(0xFFFFB300))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("✨ Popular", fontFamily = Nunito, fontWeight = FontWeight.ExtraBold,
                            fontSize = 10.sp, color = Color.White)
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(negocioNombre, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp, color = TextPrimary)
                    Text("$negocioCategoria · ${publicacion.categoria}", fontFamily = Nunito,
                        fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = TextMuted)
                }
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(BlueLight)
                        .border(1.dp, BlueBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = when (publicacion.tipo) {
                            TipoPublicacion.PRODUCTO -> "Producto"
                            TipoPublicacion.SERVICIO -> "Servicio"
                        },
                        fontFamily = Nunito, fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp, color = Blue800
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}