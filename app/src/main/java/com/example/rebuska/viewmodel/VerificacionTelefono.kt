package com.example.rebuska.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rebuska.R
import com.example.rebuska.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun VerificacionTelefonoScreen(
    telefono: String = "+57 300 000 0000",
    onVerificar: () -> Unit = {},
    onReenviar: () -> Unit = {}
) {
    var codigo by remember { mutableStateOf("") }
    val digitCount = 4

    var segundos by remember { mutableStateOf(287) }
    LaunchedEffect(Unit) {
        while (segundos > 0) { delay(1000); segundos-- }
    }
    val minutos     = segundos / 60
    val segsDisplay = segundos % 60
    val timerLabel  = "$minutos:${segsDisplay.toString().padStart(2, '0')}"
    val timerPct    = segundos / 300f

    val contentAlpha  = remember { Animatable(0f) }
    val contentOffset = remember { Animatable(30f) }
    LaunchedEffect(Unit) {
        contentAlpha.animateTo(1f,  animationSpec = tween(600))
        contentOffset.animateTo(0f, animationSpec = tween(600))
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = contentAlpha.value; translationY = contentOffset.value }
        ) {

            // ══════════════════════════════════════════
            // HEADER CON OLA
            // ══════════════════════════════════════════
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .drawBehind {
                        val path = Path().apply {
                            moveTo(0f, size.height * 0.8f)
                            quadraticBezierTo(size.width * 0.25f, size.height, size.width * 0.5f, size.height * 0.85f)
                            quadraticBezierTo(size.width * 0.75f, size.height * 0.7f, size.width, size.height * 0.85f)
                            lineTo(size.width, 0f); lineTo(0f, 0f); close()
                        }
                        drawPath(path, brush = Brush.linearGradient(
                            colors = listOf(Blue800, Blue700, Blue400),
                            start = Offset(0f, 0f), end = Offset(size.width, size.height)
                        ))
                    }
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 44.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = "Atrás",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // ══════════════════════════════════════════
            // CONTENIDO
            // ══════════════════════════════════════════
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Text("Verifica tu", fontFamily = Nunito, fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp, color = TextPrimary)
                Text("número de teléfono", fontFamily = Nunito, fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp, color = Blue800, fontStyle = FontStyle.Italic)

                Spacer(modifier = Modifier.height(12.dp))

                Text("Enviamos un SMS con código de 4 dígitos a",
                    fontFamily = Nunito, fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp, color = TextMuted)
                Text(telefono, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp, color = Blue800)

                Spacer(modifier = Modifier.height(24.dp))

                Text("Código SMS", fontFamily = Nunito,
                    fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = TextPrimary)

                Spacer(modifier = Modifier.height(16.dp))

                // OTP 4 dígitos más grandes
                OtpInputField(
                    value = codigo,
                    digitCount = digitCount,
                    onValueChange = { if (it.length <= digitCount) codigo = it },
                    boxWidth = 64.dp,
                    boxHeight = 64.dp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Timer
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TimerRing(label = timerLabel, progress = timerPct)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("El código expira en ", fontFamily = Nunito,
                        fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextMuted)
                    Text(timerLabel, fontFamily = Nunito,
                        fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = TextPrimary)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("¿No lo recibiste? ", fontFamily = Nunito,
                        fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextMuted)
                    TextButton(onClick = onReenviar, contentPadding = PaddingValues(0.dp)) {
                        Text("Reenviar SMS", fontFamily = Nunito,
                            fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = Blue800)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Disclaimer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(GoldLight)
                        .border(1.5.dp, GoldBorder, RoundedCornerShape(14.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(painter = painterResource(R.drawable.ic_info),
                        contentDescription = null, tint = Gold,
                        modifier = Modifier.size(18.dp).padding(top = 1.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "El SMS puede tardar hasta dos minutos. No compartiremos tu número con terceros.",
                        fontFamily = Nunito, fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp, color = Color(0xFF7A6000), lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = onVerificar,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue800),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text("Verificar número", fontFamily = Nunito,
                        fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// ── OTP reutilizable con tamaño configurable ──────────
@Composable
fun OtpInputField(
    value: String,
    digitCount: Int,
    onValueChange: (String) -> Unit,
    boxWidth: Dp = 46.dp,
    boxHeight: Dp = 54.dp
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        textStyle = TextStyle(color = Color.Transparent),
        decorationBox = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    10.dp,
                    Alignment.CenterHorizontally
                )
            ) {
                repeat(digitCount) { index ->
                    val char  = value.getOrNull(index)
                    val focus = index == value.length

                    Box(
                        modifier = Modifier
                            .size(width = boxWidth, height = boxHeight)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (focus || char != null) BlueLight
                                else Color(0xFFF8FAFF)
                            )
                            .border(
                                width = if (focus) 2.dp else 1.5.dp,
                                color = if (focus || char != null) Blue800 else BlueBorder,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char?.toString() ?: "—",
                            fontFamily = Nunito,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = if (char != null) 22.sp else 18.sp,
                            color = if (char != null) Blue800 else Color(0xFFBBCCEE)
                        )
                    }
                }
            }
        }
    )
}

// ── Timer circular ────────────────────────────────────
@Composable
fun TimerRing(label: String, progress: Float) {
    Box(modifier = Modifier.size(36.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxSize(),
            color = Blue800, trackColor = BlueBorder, strokeWidth = 3.dp
        )
        Text(text = label, fontFamily = Nunito, fontWeight = FontWeight.ExtraBold,
            fontSize = 8.sp, color = Blue800, textAlign = TextAlign.Center)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun VerificacionTelefonoPreview() {
    VerificacionTelefonoScreen()
}