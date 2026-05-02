package com.example.rebuska.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import coil.compose.AsyncImage
import com.example.rebuska.ui.theme.Blue400
import com.example.rebuska.ui.theme.Blue700
import com.example.rebuska.ui.theme.Blue800
import com.example.rebuska.ui.theme.BlueBorder
import com.example.rebuska.ui.theme.BlueLight
import com.example.rebuska.ui.theme.DividerColor
import com.example.rebuska.ui.theme.Nunito
import com.example.rebuska.ui.theme.TextMuted
import com.example.rebuska.ui.theme.TextPrimary

@Composable
fun TiendaScreen(
    idNegocio: String = "1",
    onAtras: () -> Unit = {},
    onContratar: () -> Unit = {},
    onChats: () -> Unit = {},
    onPerfil: () -> Unit = {},
    onHome: () -> Unit = {}
) {
    val negocio       = remember { NegocioRepository.getNegocioById(idNegocio) }
    val publicaciones = remember { PublicacionRepository.getPublicacionesByNegocio(idNegocio) }

    if (negocio == null) return

    Scaffold(
        bottomBar = {
            Column {
                // ── Botón Contactar ──
                Row(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .background(Color.Companion.White)
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    Button(
                        onClick = { onContratar() },
                        modifier = Modifier.Companion.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Blue800),
                        elevation = ButtonDefaults.buttonElevation(4.dp)
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_calendar),
                            null,
                            Modifier.Companion.size(16.dp),
                            tint = Color.Companion.White
                        )
                        Spacer(Modifier.Companion.width(6.dp))
                        Text(
                            "Contactar",
                            fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.ExtraBold,
                            fontSize = 14.sp,
                            color = Color.Companion.White
                        )
                    }
                }

                BottomNavBar(
                    seleccionado = NavDestino.HOME,
                    onHome = onHome,
                    onChats = onChats,
                    onPerfil = onPerfil,
                    onMenu = {}
                )
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier.Companion
                .fillMaxSize()
                .background(Color(0xFFF2F4F8))
                .padding(innerPadding)
        ) {

            // ── Header ───
            item {
                Box(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .background(
                            Brush.Companion.linearGradient(
                                listOf(Blue800, Blue700, Blue400),
                                Offset(0f, 0f),
                                Offset(400f, 200f)
                            )
                        )
                        .padding(start = 12.dp, end = 12.dp, top = 28.dp, bottom = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.Companion.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier.Companion.size(36.dp).clip(CircleShape)
                                .background(Color.Companion.White.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Companion.Center
                        ) {
                            IconButton(
                                onClick = { onAtras() },
                                modifier = Modifier.Companion.size(36.dp)
                            ) {
                                Icon(
                                    painterResource(R.drawable.ic_arrow_back),
                                    null,
                                    tint = Color.Companion.White,
                                    modifier = Modifier.Companion.size(18.dp)
                                )
                            }
                        }
                        Text(
                            text = negocio.nombre,
                            fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.ExtraBold,
                            fontSize = 16.sp,
                            color = Color.Companion.White,
                            modifier = Modifier.Companion.weight(1f),
                            maxLines = 1
                        )
                        Box(
                            modifier = Modifier.Companion.size(36.dp).clip(CircleShape)
                                .background(Color.Companion.White.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Companion.Center
                        ) {
                            Icon(
                                painterResource(R.drawable.ic_share),
                                null,
                                tint = Color.Companion.White,
                                modifier = Modifier.Companion.size(18.dp)
                            )
                        }
                    }
                }
            }

            // ── Banner ────────────────────────────────
            item {
                Box(modifier = Modifier.Companion.fillMaxWidth().height(170.dp)) {
                    if (negocio.bannerUrl != null) {
                        AsyncImage(
                            model = negocio.bannerUrl,
                            contentDescription = "Banner ${negocio.nombre}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        val colors = when (negocio.nombre) {
                            "Carpintería López" -> listOf(Color(0xFF5D4037), Color(0xFF8D6E63))
                            "Muebles Alta" -> listOf(Color(0xFF37474F), Color(0xFF78909C))
                            "CompuTech" -> listOf(Color(0xFF0D0D1A), Color(0xFF0A3D62))
                            else -> listOf(Blue800, Blue700)
                        }
                        val emoji = when (negocio.nombre) {
                            "Carpintería López" -> "🪵"
                            "Muebles Alta" -> "🛋️"
                            "CompuTech" -> "🖥️"
                            else -> "🏪"
                        }
                        Box(
                            modifier = Modifier.Companion.fillMaxSize().background(
                                Brush.Companion.linearGradient(
                                    colors,
                                    Offset(0f, 0f),
                                    Offset(600f, 300f)
                                )
                            ),
                            contentAlignment = Alignment.Companion.Center
                        ) { Text(emoji, fontSize = 60.sp) }
                    }
                    Box(
                        modifier = Modifier.Companion.fillMaxSize().background(
                            Brush.Companion.verticalGradient(
                                listOf(
                                    Color.Companion.Transparent,
                                    Color.Companion.Black.copy(alpha = 0.35f)
                                )
                            )
                        )
                    )
                }
            }

            // ── Perfil ─────────────────────────────────
            item {
                Box(
                    modifier = Modifier.Companion.fillMaxWidth().background(Color.Companion.White)
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Column(modifier = Modifier.Companion.fillMaxWidth()) {

                        // Fila: logo + nombre + guardar
                        Row(
                            modifier = Modifier.Companion.fillMaxWidth(),
                            verticalAlignment = Alignment.Companion.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Logo circular
                            Box(
                                modifier = Modifier.Companion.size(52.dp).clip(CircleShape)
                                    .background(Color.Companion.White)
                                    .border(2.dp, DividerColor, CircleShape),
                                contentAlignment = Alignment.Companion.Center
                            ) {
                                if (negocio.logoUrl != null) {
                                    AsyncImage(
                                        model = negocio.logoUrl,
                                        contentDescription = "Logo ${negocio.nombre}",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape)
                                    )
                                } else {
                                    val logoEmoji = when (negocio.nombre) {
                                        "Carpintería López" -> "🪚"
                                        "Muebles Alta" -> "🛋️"
                                        "CompuTech" -> "🖥️"
                                        else -> "🏪"
                                    }
                                    val logoBg = when (negocio.nombre) {
                                        "Carpintería López" -> listOf(
                                            Color(0xFF8B6914),
                                            Color(0xFFC8960A)
                                        )

                                        "Muebles Alta" -> listOf(
                                            Color(0xFF37474F),
                                            Color(0xFF78909C)
                                        )

                                        "CompuTech" -> listOf(Color(0xFF0D0D1A), Color(0xFF0A3D62))
                                        else -> listOf(Blue800, Blue700)
                                    }
                                    Box(
                                        modifier = Modifier.Companion.fillMaxSize()
                                            .clip(CircleShape).background(
                                            Brush.Companion.linearGradient(
                                                logoBg,
                                                Offset(0f, 0f),
                                                Offset(60f, 60f)
                                            )
                                        ),
                                        contentAlignment = Alignment.Companion.Center
                                    ) { Text(logoEmoji, fontSize = 24.sp) }
                                }
                            }

                            // Nombre + verificado
                            Row(
                                modifier = Modifier.Companion.weight(1f),
                                verticalAlignment = Alignment.Companion.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    negocio.nombre,
                                    fontFamily = Nunito,
                                    fontWeight = FontWeight.Companion.Black,
                                    fontSize = 18.sp,
                                    color = TextPrimary,
                                    maxLines = 2,
                                    lineHeight = 22.sp
                                )
                                if (negocio.verificado) {
                                    Box(
                                        modifier = Modifier.Companion.size(18.dp).clip(CircleShape)
                                            .background(Blue800),
                                        contentAlignment = Alignment.Companion.Center
                                    ) {
                                        Icon(
                                            painterResource(R.drawable.ic_check),
                                            null,
                                            tint = Color.Companion.White,
                                            modifier = Modifier.Companion.size(11.dp)
                                        )
                                    }
                                }
                            }

                            // Botón guardar
                            Box(
                                modifier = Modifier.Companion.size(36.dp).clip(CircleShape)
                                    .background(BlueLight).border(1.5.dp, BlueBorder, CircleShape),
                                contentAlignment = Alignment.Companion.Center
                            ) {
                                Icon(
                                    painterResource(R.drawable.ic_bookmark), null,
                                    tint = Blue800, modifier = Modifier.Companion.size(16.dp)
                                )
                            }
                        }

                        Spacer(Modifier.Companion.height(10.dp))

                        // Tags
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            TagChip(negocio.categoria)
                        }

                        Spacer(Modifier.Companion.height(12.dp))

                        // Stats con misma altura
                        Row(
                            modifier = Modifier.Companion.fillMaxWidth().height(IntrinsicSize.Max),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatChip(
                                label = "Calificación ★",
                                value = "${negocio.promCalificacion}",
                                sub = "(${negocio.totalResenas} reseñas)",
                                modifier = Modifier.Companion.weight(1f).fillMaxHeight()
                            )
                            StatChip(
                                label = "Servicios",
                                value = "203",
                                modifier = Modifier.Companion.weight(1f).fillMaxHeight()
                            )
                            StatChip(
                                label = "En RebusKa",
                                value = "3 meses",
                                modifier = Modifier.Companion.weight(1f).fillMaxHeight()
                            )
                        }
                    }
                }
            }

            // ── Sobre nosotros ────────────────────────
            item {
                SectionCard(titulo = "Sobre nosotros") {
                    Text(
                        text = negocio.descripcion,
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Companion.SemiBold,
                        fontSize = 13.sp,
                        color = Color(0xFF555555),
                        lineHeight = 20.sp
                    )
                }
            }

            // ── Título "Publicaciones" fuera del marco ──
            item {
                Row(
                    modifier = Modifier.Companion.fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.Companion.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.Companion.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier.Companion.size(6.dp).clip(CircleShape)
                                .background(Blue800)
                        )
                        Text(
                            "Publicaciones",
                            fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.ExtraBold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                    }
                    Text(
                        "Ver todas",
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Companion.ExtraBold,
                        fontSize = 11.sp,
                        color = Blue800
                    )
                }
            }

            // ── Lista de publicaciones desde el repositorio ──
            items(publicaciones) { pub ->
                PublicacionTiendaCard(pub)
            }

            item { Spacer(Modifier.Companion.height(12.dp)) }
        }
    }
}

@Composable
fun TagChip(texto: String, isGreen: Boolean = false) {
    Box(
        modifier = Modifier.Companion
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
            .background(if (isGreen) Color(0xFFE8F5E9) else BlueLight)
            .padding(horizontal = 10.dp, vertical = 3.dp)
    ) {
        Text(
            text = texto,
            fontFamily = Nunito,
            fontWeight = FontWeight.Companion.Bold,
            fontSize = 10.sp,
            color = if (isGreen) Blue700 else Blue800
        )
    }
}

@Composable
fun StatChip(label: String, value: String, sub: String? = null, modifier: Modifier = Modifier.Companion) {
    Column(
        modifier = modifier
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(14.dp))
            .background(Color.Companion.White)
            .border(1.dp, DividerColor, androidx.compose.foundation.shape.RoundedCornerShape(14.dp))
            .padding(vertical = 10.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.Companion.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            fontFamily = Nunito,
            fontWeight = FontWeight.Companion.Bold,
            fontSize = 9.sp,
            color = TextMuted,
            textAlign = TextAlign.Companion.Center,
            lineHeight = 13.sp
        )
        Spacer(Modifier.Companion.height(3.dp))
        Text(
            text = value,
            fontFamily = Nunito,
            fontWeight = FontWeight.Companion.ExtraBold,
            fontSize = 13.sp,
            color = TextPrimary,
            textAlign = TextAlign.Companion.Center
        )
        if (sub != null) {
            Text(
                text = sub,
                fontFamily = Nunito,
                fontWeight = FontWeight.Companion.SemiBold,
                fontSize = 9.sp,
                color = TextMuted,
                textAlign = TextAlign.Companion.Center,
                lineHeight = 13.sp
            )
        }
    }
}

@Composable
fun SectionCard(titulo: String, linkText: String? = null, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.Companion.fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(18.dp))
            .background(Color.Companion.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.Companion.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.Companion.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(modifier = Modifier.Companion.size(6.dp).clip(CircleShape).background(Blue800))
                Text(
                    titulo,
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.ExtraBold,
                    fontSize = 14.sp,
                    color = TextPrimary
                )
            }
            if (linkText != null) {
                Text(
                    linkText,
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.ExtraBold,
                    fontSize = 11.sp,
                    color = Blue800
                )
            }
        }
        Spacer(Modifier.Companion.height(12.dp))
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

    Row(
        modifier = Modifier.Companion.fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
            .background(Color.Companion.White)
            .border(
                1.5.dp,
                DividerColor,
                androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
            ),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        Box(
            modifier = Modifier.Companion
                .size(width = 90.dp, height = 90.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                .background(
                    Brush.Companion.linearGradient(
                        bannerColors,
                        Offset(0f, 0f),
                        Offset(100f, 100f)
                    )
                ),
            contentAlignment = Alignment.Companion.Center
        ) {
            Text(emoji, fontSize = 30.sp)
        }

        Column(
            modifier = Modifier.Companion.padding(horizontal = 12.dp, vertical = 10.dp).weight(1f)
        ) {
            Text(
                pub.titulo, fontFamily = Nunito, fontWeight = FontWeight.Companion.ExtraBold,
                fontSize = 13.sp, color = TextPrimary, maxLines = 2, lineHeight = 18.sp
            )
            Spacer(Modifier.Companion.height(3.dp))
            Text(
                pub.descripcion, fontFamily = Nunito, fontWeight = FontWeight.Companion.SemiBold,
                fontSize = 11.sp, color = TextMuted, maxLines = 2, lineHeight = 15.sp
            )
            Spacer(Modifier.Companion.height(6.dp))

            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                verticalAlignment = Alignment.Companion.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$${"%,d".format(pub.precio)}",
                    fontFamily = Nunito, fontWeight = FontWeight.Companion.ExtraBold,
                    fontSize = 14.sp, color = Blue800
                )
                Box(
                    modifier = Modifier.Companion
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
                        .background(if (pub.tipo == TipoPublicacion.SERVICIO) Color(0xFFFFF3E0) else BlueLight)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (pub.tipo == TipoPublicacion.SERVICIO) "Servicio" else "Producto",
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Companion.Bold,
                        fontSize = 9.sp,
                        color = if (pub.tipo == TipoPublicacion.SERVICIO) Color(0xFFE65100) else Blue800
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TiendaScreenPreview() {
    TiendaScreen()
}