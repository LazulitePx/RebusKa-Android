package com.example.rebuska.ui.screens.login

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rebuska.R
import com.example.rebuska.ui.theme.Blue400
import com.example.rebuska.ui.theme.Blue700
import com.example.rebuska.ui.theme.Blue800
import com.example.rebuska.ui.theme.BlueBorder
import com.example.rebuska.ui.theme.BlueLight
import com.example.rebuska.ui.theme.Gold
import com.example.rebuska.ui.theme.GoldBorder
import com.example.rebuska.ui.theme.GoldLight
import com.example.rebuska.ui.theme.Nunito
import com.example.rebuska.ui.theme.TextMuted
import com.example.rebuska.ui.theme.TextPrimary

enum class RolUsuario { CLIENTE, TRABAJADOR }

@Composable
fun RegistroRolScreen(
    onContinuar: (RolUsuario) -> Unit = {},
    onIniciarSesion: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var rolSeleccionado by remember { mutableStateOf(RolUsuario.CLIENTE) }

    val contentAlpha  = remember { Animatable(0f) }
    val contentOffset = remember { Animatable(30f) }

    LaunchedEffect(Unit) {
        contentAlpha.animateTo(1f, animationSpec = tween(600))
        contentOffset.animateTo(0f, animationSpec = tween(600))
    }

    Box(
        modifier = Modifier.Companion
            .fillMaxSize()
            .background(Color.Companion.White)
    ) {
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .graphicsLayer {
                    alpha = contentAlpha.value
                    translationY = contentOffset.value
                }
        ) {

            // ══════════════════════════════════════════
            // HEADER AZUL CON OLA
            // ══════════════════════════════════════════
            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(140.dp)
                    .drawBehind {
                        val path = Path().apply {
                            moveTo(0f, size.height * 0.8f)
                            quadraticBezierTo(
                                size.width * 0.25f, size.height,
                                size.width * 0.5f, size.height * 0.85f
                            )
                            quadraticBezierTo(
                                size.width * 0.75f, size.height * 0.7f,
                                size.width, size.height * 0.85f
                            )
                            lineTo(size.width, 0f)
                            lineTo(0f, 0f)
                            close()
                        }
                        drawPath(
                            path = path,
                            brush = Brush.Companion.linearGradient(
                                colors = listOf(Blue800, Blue700, Blue400),
                                start = Offset(0f, 0f),
                                end = Offset(size.width, size.height)
                            )
                        )
                    }
            ) {
                //Botón atrás
                Box(
                    modifier = Modifier.Companion
                        .padding(start = 16.dp, top = 44.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.Companion.White.copy(alpha = 0.18f))
                        .clickable { onBack() },
                    contentAlignment = Alignment.Companion.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = "Atrás",
                        tint = Color.Companion.White,
                        modifier = Modifier.Companion.size(18.dp)
                    )
                }
            }

            // ══════════════════════════════════════════
            // CONTENIDO
            // ══════════════════════════════════════════
            Column(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .background(Color.Companion.White)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {

                Spacer(modifier = Modifier.Companion.height(24.dp))

                // Título
                Text(
                    text = "Hola,",
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.ExtraBold,
                    fontSize = 24.sp,
                    color = TextPrimary
                )
                Text(
                    text = "Crea tu cuenta",
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.ExtraBold,
                    fontSize = 22.sp,
                    color = Blue800
                )

                Spacer(modifier = Modifier.Companion.height(4.dp))

                Text(
                    text = "¿Cómo quieres registrarte?",
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.SemiBold,
                    fontSize = 13.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.Companion.height(24.dp))

                Text(
                    text = "Selecciona tu perfil",
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.Companion.height(14.dp))

                // ── Cards de rol ──────────────────────
                Row(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    RolCard(
                        emoji = "👤",
                        nombre = "Cliente",
                        descripcion = "Busco y contrato servicios",
                        seleccionado = rolSeleccionado == RolUsuario.CLIENTE,
                        modifier = Modifier.Companion.weight(1f),
                        onClick = { rolSeleccionado = RolUsuario.CLIENTE }
                    )
                    RolCard(
                        emoji = "👷",
                        nombre = "Trabajador",
                        descripcion = "Ofrezco mis productos o habilidades",
                        seleccionado = rolSeleccionado == RolUsuario.TRABAJADOR,
                        modifier = Modifier.Companion.weight(1f),
                        onClick = { rolSeleccionado = RolUsuario.TRABAJADOR }
                    )
                }

                Spacer(modifier = Modifier.Companion.height(24.dp))

                // ── Disclaimer ────────────────────────
                Row(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(GoldLight)
                        .border(
                            1.5.dp,
                            GoldBorder,
                            androidx.compose.foundation.shape.RoundedCornerShape(14.dp)
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.Companion.Top
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_info),
                        contentDescription = null,
                        tint = Gold,
                        modifier = Modifier.Companion
                            .size(18.dp)
                            .padding(top = 1.dp)
                    )
                    Spacer(modifier = Modifier.Companion.width(10.dp))
                    Text(
                        text = "Elige correctamente tu rol — no podrás cambiarlo después del registro. El rol define las funciones que tendrás dentro de la aplicación.",
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Companion.SemiBold,
                        fontSize = 11.5.sp,
                        color = Color(0xFF7A6000),
                        lineHeight = 17.sp
                    )
                }

                Spacer(modifier = Modifier.Companion.height(24.dp))

                // ── Botón Continuar ───────────────────
                Button(
                    onClick = { onContinuar(rolSeleccionado) },
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue800),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "Continuar",
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Companion.ExtraBold,
                        fontSize = 15.sp,
                        color = Color.Companion.White
                    )
                }

                Spacer(modifier = Modifier.Companion.height(24.dp))

                // ¿Ya tienes cuenta?
                Row(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    Text(
                        text = "¿Ya tienes una cuenta? ",
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Companion.SemiBold,
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "Iniciar sesión",
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Companion.ExtraBold,
                        fontSize = 13.sp,
                        color = Blue800,
                        modifier = Modifier.Companion.clickable { onIniciarSesion() }
                    )
                }

                Spacer(modifier = Modifier.Companion.height(24.dp))
            }
        }
    }
}

// ── Componente de card de rol ─────────────────────────
@Composable
fun RolCard(
    emoji: String,
    nombre: String,
    descripcion: String,
    seleccionado: Boolean,
    modifier: Modifier = Modifier.Companion,
    onClick: () -> Unit
) {
    val borderColor     = if (seleccionado) Blue800 else BlueBorder
    val backgroundColor = if (seleccionado) BlueLight else Color(0xFFF8FAFF)
    val iconBg          = if (seleccionado) Color(0xFFBFD7FF) else Color(0xFFDDEEFF)

    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(2.dp, borderColor, androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(vertical = 20.dp, horizontal = 12.dp)
    ) {
        // Check de seleccionado
        if (seleccionado) {
            Box(
                modifier = Modifier.Companion
                    .align(Alignment.Companion.TopEnd)
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(Blue800),
                contentAlignment = Alignment.Companion.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = Color.Companion.White,
                    modifier = Modifier.Companion.size(13.dp)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
            modifier = Modifier.Companion.fillMaxWidth()
        ) {
            // Ícono
            Box(
                modifier = Modifier.Companion
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Companion.Center
            ) {
                Text(text = emoji, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.Companion.height(10.dp))

            Text(
                text = nombre,
                fontFamily = Nunito,
                fontWeight = FontWeight.Companion.ExtraBold,
                fontSize = 14.sp,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.Companion.height(4.dp))

            Text(
                text = descripcion,
                fontFamily = Nunito,
                fontWeight = FontWeight.Companion.SemiBold,
                fontSize = 10.5.sp,
                color = TextMuted,
                textAlign = TextAlign.Companion.Center,
                lineHeight = 14.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegistroRolPreview() {
    RegistroRolScreen()
}