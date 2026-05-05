package com.example.rebuska.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.rebuska.R
import com.example.rebuska.data.model.Negocio
import com.example.rebuska.data.model.Publicacion
import com.example.rebuska.ui.components.BottomNavBar
import com.example.rebuska.ui.components.NavDestino
import com.example.rebuska.ui.theme.*
import com.example.rebuska.viewmodel.TiendaUiState
import com.example.rebuska.viewmodel.TiendaViewModel

@Composable
fun TiendaScreen(
    idNegocio: String = "",
    onAtras: () -> Unit = {},
    onContratar: () -> Unit = {},
    onChats: () -> Unit = {},
    onPerfil: () -> Unit = {},
    onHome: () -> Unit = {},
    viewModel: TiendaViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(idNegocio) {
        viewModel.cargar(idNegocio)
    }

    val negocio = when (val s = uiState) {
        is TiendaUiState.Exito -> s.negocio
        else -> Negocio()
    }
    val publicaciones = when (val s = uiState) {
        is TiendaUiState.Exito -> s.publicaciones
        else -> emptyList()
    }

    Scaffold(
        bottomBar = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().background(Color.White)
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Button(
                        onClick = onContratar,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Blue800),
                        elevation = ButtonDefaults.buttonElevation(4.dp)
                    ) {
                        Icon(painterResource(R.drawable.ic_calendar), null,
                            Modifier.size(16.dp), tint = Color.White)
                        Spacer(Modifier.width(6.dp))
                        Text("Contactar", fontFamily = Nunito, fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp, color = Color.White)
                    }
                }
                BottomNavBar(
                    seleccionado = NavDestino.HOME,
                    onHome   = onHome,
                    onChats  = onChats,
                    onPerfil = onPerfil,
                    onMenu   = {}
                )
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .background(Color(0xFFF2F4F8))
                .padding(innerPadding)
        ) {

            // ── Estado cargando / error
            when (val s = uiState) {
                is TiendaUiState.Cargando -> item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Blue800)
                    }
                }
                is TiendaUiState.Error -> item {
                    Text("⚠️ ${s.mensaje}", color = Color.Red, modifier = Modifier.padding(16.dp))
                }
                else -> {}
            }

            // ── Header
            item {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .background(Brush.linearGradient(
                            listOf(Blue800, Blue700, Blue400),
                            Offset(0f, 0f), Offset(400f, 200f)))
                        .padding(start = 12.dp, end = 12.dp, top = 28.dp, bottom = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier.size(36.dp).clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(onClick = onAtras, modifier = Modifier.size(36.dp)) {
                                Icon(painterResource(R.drawable.ic_arrow_back), null,
                                    tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                        Text(negocio.nombre.ifEmpty { "Cargando..." },
                            fontFamily = Nunito, fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp, color = Color.White,
                            modifier = Modifier.weight(1f), maxLines = 1)
                        Box(
                            modifier = Modifier.size(36.dp).clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(painterResource(R.drawable.ic_share), null,
                                tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            // ── Banner
            item {
                val bannerColors = when (negocio.nombre) {
                    "Carpintería López" -> listOf(Color(0xFF5D4037), Color(0xFF8D6E63))
                    "Muebles Alta"      -> listOf(Color(0xFF37474F), Color(0xFF78909C))
                    "CompuTech"         -> listOf(Color(0xFF0D0D1A), Color(0xFF0A3D62))
                    else                -> listOf(Blue800, Blue700)
                }
                val bannerEmoji = when (negocio.nombre) {
                    "Carpintería López" -> "🪵"
                    "Muebles Alta"      -> "🛋️"
                    "CompuTech"         -> "🖥️"
                    else                -> "🏪"
                }
                Box(modifier = Modifier.fillMaxWidth().height(170.dp)) {
                    if (negocio.bannerUrl.isNotEmpty()) {
                        AsyncImage(model = negocio.bannerUrl,
                            contentDescription = "Banner ${negocio.nombre}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize())
                    } else {
                        Box(modifier = Modifier.fillMaxSize().background(
                            Brush.linearGradient(bannerColors, Offset(0f, 0f), Offset(600f, 300f))),
                            contentAlignment = Alignment.Center
                        ) { Text(bannerEmoji, fontSize = 60.sp) }
                    }
                    Box(modifier = Modifier.fillMaxSize().background(
                        Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.35f)))))
                }
            }

            // ── Perfil del negocio
            item {
                Box(modifier = Modifier.fillMaxWidth().background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 14.dp)) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                            val logoEmoji = when (negocio.nombre) {
                                "Carpintería López" -> "🪚"
                                "Muebles Alta"      -> "🛋️"
                                "CompuTech"         -> "🖥️"
                                else                -> "🏪"
                            }
                            val logoBg = when (negocio.nombre) {
                                "Carpintería López" -> listOf(Color(0xFF8B6914), Color(0xFFC8960A))
                                "Muebles Alta"      -> listOf(Color(0xFF37474F), Color(0xFF78909C))
                                "CompuTech"         -> listOf(Color(0xFF0D0D1A), Color(0xFF0A3D62))
                                else                -> listOf(Blue800, Blue700)
                            }
                            Box(
                                modifier = Modifier.size(52.dp).clip(CircleShape)
                                    .background(Brush.linearGradient(logoBg, Offset(0f,0f), Offset(60f,60f)))
                                    .border(2.dp, DividerColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (negocio.logoUrl.isNotEmpty()) {
                                    AsyncImage(model = negocio.logoUrl,
                                        contentDescription = "Logo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize().clip(CircleShape))
                                } else {
                                    Text(logoEmoji, fontSize = 24.sp)
                                }
                            }

                            Row(modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(negocio.nombre, fontFamily = Nunito,
                                    fontWeight = FontWeight.Black, fontSize = 18.sp,
                                    color = TextPrimary, maxLines = 2, lineHeight = 22.sp)
                                if (negocio.verificado) {
                                    Box(modifier = Modifier.size(18.dp).clip(CircleShape)
                                        .background(Blue800),
                                        contentAlignment = Alignment.Center) {
                                        Icon(painterResource(R.drawable.ic_check), null,
                                            tint = Color.White, modifier = Modifier.size(11.dp))
                                    }
                                }
                            }

                            Box(modifier = Modifier.size(36.dp).clip(CircleShape)
                                .background(BlueLight).border(1.5.dp, BlueBorder, CircleShape),
                                contentAlignment = Alignment.Center) {
                                Icon(painterResource(R.drawable.ic_bookmark), null,
                                    tint = Blue800, modifier = Modifier.size(16.dp))
                            }
                        }

                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            TagChip(negocio.categoria)
                        }
                        Spacer(Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            StatChip(label = "Calificación ★",
                                value = "${negocio.promCalificacion}",
                                sub = "(${negocio.totalResenas} reseñas)",
                                modifier = Modifier.weight(1f).fillMaxHeight())
                            StatChip(label = "Servicios", value = "203",
                                modifier = Modifier.weight(1f).fillMaxHeight())
                            StatChip(label = "En RebusKa", value = "3 meses",
                                modifier = Modifier.weight(1f).fillMaxHeight())
                        }
                    }
                }
            }

            // ── Sobre nosotros
            item {
                SectionCard(titulo = "Sobre nosotros") {
                    Text(negocio.descripcion, fontFamily = Nunito,
                        fontWeight = FontWeight.SemiBold, fontSize = 13.sp,
                        color = Color(0xFF555555), lineHeight = 20.sp)
                }
            }

            // ── Título publicaciones
            item {
                Row(modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Blue800))
                        Text("Publicaciones", fontFamily = Nunito,
                            fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = TextPrimary)
                    }
                    Text("Ver todas", fontFamily = Nunito, fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp, color = Blue800)
                }
            }

            items(publicaciones) { pub -> PublicacionTiendaCard(pub) }

            item { Spacer(Modifier.height(12.dp)) }
        }
    }
}

// ── Componentes auxiliares ────────────────────────────

@Composable
fun TagChip(texto: String, isGreen: Boolean = false) {
    Box(modifier = Modifier.clip(RoundedCornerShape(20.dp))
        .background(if (isGreen) Color(0xFFE8F5E9) else BlueLight)
        .padding(horizontal = 10.dp, vertical = 3.dp)) {
        Text(texto, fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 10.sp,
            color = if (isGreen) Blue700 else Blue800)
    }
}

@Composable
fun StatChip(label: String, value: String, sub: String? = null, modifier: Modifier = Modifier) {
    Column(modifier = modifier.clip(RoundedCornerShape(14.dp)).background(Color.White)
        .border(1.dp, DividerColor, RoundedCornerShape(14.dp))
        .padding(vertical = 10.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(label, fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 9.sp,
            color = TextMuted, textAlign = TextAlign.Center, lineHeight = 13.sp)
        Spacer(Modifier.height(3.dp))
        Text(value, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp,
            color = TextPrimary, textAlign = TextAlign.Center)
        if (sub != null) {
            Text(sub, fontFamily = Nunito, fontWeight = FontWeight.SemiBold, fontSize = 9.sp,
                color = TextMuted, textAlign = TextAlign.Center, lineHeight = 13.sp)
        }
    }
}

@Composable
fun SectionCard(titulo: String, linkText: String? = null,
                content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()
        .padding(horizontal = 10.dp, vertical = 4.dp)
        .clip(RoundedCornerShape(18.dp)).background(Color.White).padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Blue800))
                Text(titulo, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp, color = TextPrimary)
            }
            if (linkText != null) {
                Text(linkText, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp, color = Blue800)
            }
        }
        Spacer(Modifier.height(12.dp))
        content()
    }
}

@Composable
fun PublicacionTiendaCard(pub: Publicacion) {
    val bannerColors = when (pub.categoria) {
        "Carpintería" -> listOf(Color(0xFF5D4037), Color(0xFF8D6E63))
        "Hogar"       -> listOf(Color(0xFF37474F), Color(0xFF78909C))
        "Tecnología"  -> listOf(Color(0xFF0D0D1A), Color(0xFF0A3D62))
        else          -> listOf(Blue800, Blue700)
    }
    val emoji = when (pub.categoria) {
        "Carpintería" -> "🪚"
        "Hogar"       -> "🛋️"
        "Tecnología"  -> "💻"
        else          -> "📦"
    }

    Row(modifier = Modifier.fillMaxWidth()
        .padding(horizontal = 10.dp, vertical = 4.dp)
        .clip(RoundedCornerShape(16.dp)).background(Color.White)
        .border(1.5.dp, DividerColor, RoundedCornerShape(16.dp)),
        verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(width = 90.dp, height = 90.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
            .background(Brush.linearGradient(bannerColors, Offset(0f,0f), Offset(100f,100f))),
            contentAlignment = Alignment.Center) {
            if (pub.fotoUrl.isNotEmpty()) {
                AsyncImage(model = pub.fotoUrl, contentDescription = null,
                    contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            } else {
                Text(emoji, fontSize = 30.sp)
            }
        }

        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp).weight(1f)) {
            Text(pub.titulo, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp, color = TextPrimary, maxLines = 2, lineHeight = 18.sp)
            Spacer(Modifier.height(3.dp))
            Text(pub.descripcion, fontFamily = Nunito, fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp, color = TextMuted, maxLines = 2, lineHeight = 15.sp)
            Spacer(Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text("$${"%,d".format(pub.precio)}", fontFamily = Nunito,
                    fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = Blue800)
                val esServicio = pub.tipo == "SERVICIO"
                Box(modifier = Modifier.clip(RoundedCornerShape(20.dp))
                    .background(if (esServicio) Color(0xFFFFF3E0) else BlueLight)
                    .padding(horizontal = 8.dp, vertical = 3.dp)) {
                    Text(if (esServicio) "Servicio" else "Producto",
                        fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 9.sp,
                        color = if (esServicio) Color(0xFFE65100) else Blue800)
                }
            }
        }
    }
}