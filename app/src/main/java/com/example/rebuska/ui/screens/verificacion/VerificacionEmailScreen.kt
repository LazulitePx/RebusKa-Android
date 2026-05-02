package com.example.rebuska.ui.screens.verificacion

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import kotlinx.coroutines.delay

@Composable
fun VerificacionEmailScreen(
    email: String = "correo@ejemplo.com",
    onVerificar: () -> Unit = {},
    onReenviar: () -> Unit = {}
) {
    var codigo by remember { mutableStateOf("") }
    val digitCount = 6

    // Timer 5 minutos
    var segundos by remember { mutableStateOf(287) }
    LaunchedEffect(Unit) {
        while (segundos > 0) {
            delay(1000)
            segundos--
        }
    }
    val minutos     = segundos / 60
    val segsDisplay = segundos % 60
    val timerLabel  = "$minutos:${segsDisplay.toString().padStart(2, '0')}"
    val timerPct    = segundos / 300f

    val contentAlpha  = remember { Animatable(0f) }
    val contentOffset = remember { Animatable(30f) }
    LaunchedEffect(Unit) {
        contentAlpha.animateTo(1f, animationSpec = tween(600))
        contentOffset.animateTo(0f, animationSpec = tween(600))
    }

    Box(modifier = Modifier.Companion.fillMaxSize().background(Color.Companion.White)) {
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .graphicsLayer { alpha = contentAlpha.value; translationY = contentOffset.value }
        ) {

            // ══════════════════════════════════════════
            // HEADER CON OLA
            // ══════════════════════════════════════════
            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(140.dp)
                    .drawBehind {
                        val path = Path().apply {
                            moveTo(0f, size.height * 0.8f)
                            quadraticBezierTo(
                                size.width * 0.25f,
                                size.height,
                                size.width * 0.5f,
                                size.height * 0.85f
                            )
                            quadraticBezierTo(
                                size.width * 0.75f,
                                size.height * 0.7f,
                                size.width,
                                size.height * 0.85f
                            )
                            lineTo(size.width, 0f); lineTo(0f, 0f); close()
                        }
                        drawPath(
                            path, brush = Brush.Companion.linearGradient(
                                colors = listOf(Blue800, Blue700, Blue400),
                                start = Offset(0f, 0f), end = Offset(size.width, size.height)
                            )
                        )
                    }
            ) {
                Box(
                    modifier = Modifier.Companion
                        .padding(start = 16.dp, top = 44.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.Companion.White.copy(alpha = 0.18f)),
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

                Text(
                    "Verifica tu", fontFamily = Nunito, fontWeight = FontWeight.Companion.ExtraBold,
                    fontSize = 26.sp, color = TextPrimary
                )
                Text(
                    "Correo electrónico",
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.ExtraBold,
                    fontSize = 24.sp,
                    color = Blue800,
                    fontStyle = FontStyle.Companion.Italic
                )

                Spacer(modifier = Modifier.Companion.height(12.dp))

                Text(
                    text = "Enviamos un código de 6 dígitos a",
                    fontFamily = Nunito, fontWeight = FontWeight.Companion.SemiBold,
                    fontSize = 13.sp, color = TextMuted
                )
                Text(
                    text = email,
                    fontFamily = Nunito, fontWeight = FontWeight.Companion.ExtraBold,
                    fontSize = 13.sp, color = Blue800
                )
                Text(
                    text = "No olvides revisar en tu carpeta de spam.",
                    fontFamily = Nunito, fontWeight = FontWeight.Companion.SemiBold,
                    fontSize = 12.sp, color = TextMuted
                )

                Spacer(modifier = Modifier.Companion.height(24.dp))

                Text(
                    "Código de verificación",
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.ExtraBold,
                    fontSize = 14.sp,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.Companion.height(16.dp))

                // Código 6 dígitos
                OtpField(
                    value = codigo,
                    digitCount = digitCount,
                    onValueChange = { if (it.length <= digitCount) codigo = it }
                )

                Spacer(modifier = Modifier.Companion.height(20.dp))

                // Timer
                Row(
                    verticalAlignment = Alignment.Companion.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.Companion.fillMaxWidth()
                ) {
                    TimerCircle(label = timerLabel, progress = timerPct)
                    Spacer(modifier = Modifier.Companion.width(10.dp))
                    Text(
                        text = "El código expira en ",
                        fontFamily = Nunito, fontWeight = FontWeight.Companion.SemiBold,
                        fontSize = 13.sp, color = TextMuted
                    )
                    Text(
                        text = timerLabel,
                        fontFamily = Nunito, fontWeight = FontWeight.Companion.ExtraBold,
                        fontSize = 13.sp, color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.Companion.height(14.dp))

                // Reenviar
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Companion.CenterVertically,
                    modifier = Modifier.Companion.fillMaxWidth()
                ) {
                    Text(
                        "¿No lo recibiste? ",
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Companion.SemiBold,
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                    TextButton(
                        onClick = onReenviar,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            "Reenviar código",
                            fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.ExtraBold,
                            fontSize = 13.sp,
                            color = Blue800
                        )
                    }
                }

                Spacer(modifier = Modifier.Companion.height(16.dp))

                // Disclaimer
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
                        contentDescription = null, tint = Gold,
                        modifier = Modifier.Companion.size(18.dp).padding(top = 1.dp)
                    )
                    Spacer(modifier = Modifier.Companion.width(10.dp))
                    Text(
                        text = "Este código es de un solo uso. Nunca compartas este código con nadie.",
                        fontFamily = Nunito, fontWeight = FontWeight.Companion.SemiBold,
                        fontSize = 12.sp, color = Color(0xFF7A6000), lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.Companion.weight(1f))
                Spacer(modifier = Modifier.Companion.height(28.dp))

                Button(
                    onClick = onVerificar,
                    modifier = Modifier.Companion.fillMaxWidth().height(52.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue800),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        "Verificar correo",
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Companion.ExtraBold,
                        fontSize = 15.sp,
                        color = Color.Companion.White
                    )
                }

                Spacer(modifier = Modifier.Companion.height(24.dp))
            }
        }
    }
}

// ── Componente OTP reutilizable ───────────────────────
@Composable
fun OtpField(
    value: String,
    digitCount: Int,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Companion.NumberPassword),
        textStyle = TextStyle(color = Color.Companion.Transparent),
        decorationBox = {
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    10.dp,
                    Alignment.Companion.CenterHorizontally
                )
            ) {
                repeat(digitCount) { index ->
                    val char = value.getOrNull(index)
                    val focus = index == value.length

                    Box(
                        modifier = Modifier.Companion
                            .size(40.dp, 54.dp),
                        contentAlignment = Alignment.Companion.Center
                    ) {
                        Box(
                            modifier = Modifier.Companion
                                .matchParentSize()
                                .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                                .background(
                                    if (focus || char != null) BlueLight
                                    else Color(0xFFF8FAFF)
                                )
                                .border(
                                    width = if (focus) 2.dp else 1.5.dp,
                                    color = if (focus || char != null) Blue800 else BlueBorder,
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Companion.Center
                        ) {
                            Text(
                                text = char?.toString() ?: "—",
                                fontFamily = Nunito,
                                fontWeight = FontWeight.Companion.ExtraBold,
                                fontSize = if (char != null) 20.sp else 16.sp,
                                color = if (char != null) Blue800 else Color(0xFFBBCCEE)
                            )
                        }
                    }
                }
            }
        }
    )
}

// ── Timer circular ────────────────────────────────────
@Composable
fun TimerCircle(label: String, progress: Float) {
    Box(
        modifier = Modifier.Companion.size(36.dp),
        contentAlignment = Alignment.Companion.Center
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.Companion.fillMaxSize(),
            color = Blue800,
            trackColor = BlueBorder,
            strokeWidth = 3.dp
        )
        Text(
            text = label,
            fontFamily = Nunito,
            fontWeight = FontWeight.Companion.ExtraBold,
            fontSize = 8.sp,
            color = Blue800,
            textAlign = TextAlign.Companion.Center
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VerificacionEmailPreview() {
    VerificacionEmailScreen()
}