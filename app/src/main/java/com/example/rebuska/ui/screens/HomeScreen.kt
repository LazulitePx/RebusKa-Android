package com.example.rebuska.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
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
import com.example.rebuska.ui.theme.Blue400
import com.example.rebuska.ui.theme.Blue700
import com.example.rebuska.ui.theme.Blue800
import com.example.rebuska.ui.theme.BlueBorder
import com.example.rebuska.ui.theme.BlueLight
import com.example.rebuska.ui.theme.Nunito
import com.example.rebuska.ui.theme.TextMuted
import com.example.rebuska.ui.theme.TextPrimary

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
    onVerTienda: (idNegocio: String) -> Unit = {},
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
                onHome = {},
                onChats = onChats,   // 🔥 importante
                onLogo = onLogin,
                onPerfil = onPerfil,
                onMenu = {}
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier.Companion
                .fillMaxSize()
                .background(Color(0xFFF2F4F8))
                .padding(innerPadding)
        ) {

            // ── Header azul  ──────────────
            item {
                Box(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .background(
                            Brush.Companion.linearGradient(
                                listOf(Blue800, Blue700, Blue400),
                                Offset(0f, 0f),
                                Offset(400f, 300f)
                            )
                        )

                        .padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.Companion.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Buscador
                        OutlinedTextField(
                            value = busqueda,
                            onValueChange = { busqueda = it },
                            placeholder = {
                                Text(
                                    "Buscar producto o servicio...",
                                    fontFamily = Nunito, fontWeight = FontWeight.Companion.SemiBold,
                                    fontSize = 13.sp, color = Color(0xFFAAAAAA)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    painterResource(R.drawable.ic_search),
                                    null,
                                    tint = Color(0xFFAAAAAA),
                                    modifier = Modifier.Companion.size(18.dp)
                                )
                            },
                            singleLine = true,
                            modifier = Modifier.Companion.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(50.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Companion.Transparent,
                                unfocusedBorderColor = Color.Companion.Transparent,
                                focusedContainerColor = Color.Companion.White,
                                unfocusedContainerColor = Color.Companion.White,
                                cursorColor = Blue800
                            )
                        )
                        // Campana de notificaciones
                        Box(contentAlignment = Alignment.Companion.TopEnd) {
                            Box(
                                modifier = Modifier.Companion
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.Companion.White.copy(alpha = 0.18f)),
                                contentAlignment = Alignment.Companion.Center
                            ) {
                                Icon(
                                    painterResource(R.drawable.ic_notifications),
                                    null,
                                    tint = Color.Companion.White,
                                    modifier = Modifier.Companion.size(20.dp)
                                )
                            }
                            Box(
                                modifier = Modifier.Companion
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE53935))
                                    .offset(x = 2.dp, y = (-2).dp),
                                contentAlignment = Alignment.Companion.Center
                            ) {
                                Text(
                                    "3",
                                    fontFamily = Nunito,
                                    fontWeight = FontWeight.Companion.ExtraBold,
                                    fontSize = 8.sp,
                                    color = Color.Companion.White
                                )
                            }
                        }
                    }
                }
            }

            // ── Banner promocional ────────────────────
            item {
                Box(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 14.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
                        .background(Color(0xFFFF8E27))
                        .height(150.dp)
                ) {
                    Row(
                        modifier = Modifier.Companion.fillMaxSize().padding(start = 20.dp),
                        verticalAlignment = Alignment.Companion.CenterVertically
                    ) {
                        Column(modifier = Modifier.Companion.weight(1f)) {
                            Text(
                                text = "¡La solución\na tu alcance!",
                                fontFamily = Nunito, fontWeight = FontWeight.Companion.Black,
                                fontSize = 20.sp, color = Color.Companion.White, lineHeight = 25.sp
                            )
                            Spacer(Modifier.Companion.height(12.dp))
                            Box(
                                modifier = Modifier.Companion
                                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(50.dp))
                                    .background(Blue800)
                                    .padding(horizontal = 16.dp, vertical = 7.dp)
                            ) {
                                Text(
                                    "Buscar ofertas",
                                    fontFamily = Nunito,
                                    fontWeight = FontWeight.Companion.ExtraBold,
                                    fontSize = 12.sp,
                                    color = Color.Companion.White
                                )
                            }
                        }
                        Image(
                            painter = painterResource(R.drawable.ic_banner),
                            contentDescription = "Trabajador",
                            contentScale = ContentScale.Companion.Fit,
                            modifier = Modifier.Companion.width(195.dp).fillMaxHeight()
                        )
                    }
                }
            }

            // ── Categorías ────────────────────────────
            item {
                Column(modifier = Modifier.Companion.padding(horizontal = 14.dp)) {
                    Row(
                        modifier = Modifier.Companion.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Companion.CenterVertically
                    ) {
                        Text(
                            "Categorías",
                            fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.ExtraBold,
                            fontSize = 17.sp,
                            color = TextPrimary
                        )
                        TextButton(
                            onClick = onVerCategorias,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                "Ver todas >",
                                fontFamily = Nunito,
                                fontWeight = FontWeight.Companion.ExtraBold,
                                fontSize = 12.sp,
                                color = Blue800
                            )
                        }
                    }
                    Spacer(Modifier.Companion.height(10.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(categorias) { cat ->
                            Column(
                                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                                modifier = Modifier.Companion.width(68.dp)
                            ) {
                                Box(
                                    modifier = Modifier.Companion.size(56.dp).clip(
                                        androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                                    ).background(Color.Companion.White),
                                    contentAlignment = Alignment.Companion.Center
                                ) { Text(cat.emoji, fontSize = 24.sp) }
                                Spacer(Modifier.Companion.height(6.dp))
                                Text(
                                    cat.nombre,
                                    fontFamily = Nunito,
                                    fontWeight = FontWeight.Companion.Bold,
                                    fontSize = 10.sp,
                                    color = TextPrimary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                    Spacer(Modifier.Companion.height(20.dp))
                }
            }

            // ── Título ofertas ────────────────────────
            item {
                Text(
                    "Ofertas disponibles",
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.ExtraBold,
                    fontSize = 17.sp,
                    color = TextPrimary,
                    modifier = Modifier.Companion.padding(horizontal = 14.dp)
                )
                Spacer(Modifier.Companion.height(12.dp))
            }

            // ── 1 card por negocio ────────────────────
            items(negocios) { negocio ->
                val primeraPublicacion = PublicacionRepository
                    .getPublicacionesByNegocio(negocio.id)
                    .firstOrNull()

                if (primeraPublicacion != null) {
                    PublicacionCardHome(
                        publicacion = primeraPublicacion,
                        negocioNombre = negocio.nombre,
                        negocioCategoria = negocio.categoria,
                        bannerUrl = null,
                        esPopular = negocio.id == "1",
                        onClick = { onVerTienda(negocio.id) }
                    )
                }
            }

            item { Spacer(Modifier.Companion.height(12.dp)) }
        }
    }
}

@Composable
fun PublicacionCardHome(
    publicacion: Publicacion,
    negocioNombre: String,
    negocioCategoria: String,
    bannerUrl: String?,
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
        modifier = Modifier.Companion.fillMaxWidth().padding(horizontal = 14.dp, vertical = 6.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Companion.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier.Companion.fillMaxWidth().height(155.dp)) {
                if (bannerUrl != null) {
                        // Uso de Coil a futuro
                        Text("Imagen desde URL")
                } else {
                    Box(
                        modifier = Modifier.Companion.fillMaxSize().background(
                            Brush.Companion.linearGradient(
                                bannerColors,
                                Offset(0f, 0f),
                                Offset(600f, 300f)
                            )
                        ),
                        contentAlignment = Alignment.Companion.Center
                    ) { Text(bannerEmoji, fontSize = 60.sp) }
                }
                if (esPopular) {
                    Box(
                        modifier = Modifier.Companion.align(Alignment.Companion.TopEnd)
                            .padding(10.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
                            .background(Color(0xFFFFB300))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "✨ Popular",
                            fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.ExtraBold,
                            fontSize = 10.sp,
                            color = Color.Companion.White
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.Companion.fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                Column(modifier = Modifier.Companion.weight(1f)) {
                    Text(
                        negocioNombre,
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Companion.ExtraBold,
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                    Text(
                        "$negocioCategoria · ${publicacion.categoria}",
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Companion.SemiBold,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
                Box(
                    modifier = Modifier.Companion.clip(
                        androidx.compose.foundation.shape.RoundedCornerShape(
                            20.dp
                        )
                    ).background(BlueLight)
                        .border(
                            1.dp,
                            BlueBorder,
                            androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = when (publicacion.tipo) {
                            TipoPublicacion.PRODUCTO -> "Producto"
                            TipoPublicacion.SERVICIO -> "Servicio"
                        },
                        fontFamily = Nunito, fontWeight = FontWeight.Companion.ExtraBold,
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