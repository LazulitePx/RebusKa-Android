package com.example.rebuska.ui.screens.login

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rebuska.R
import com.example.rebuska.ui.theme.Blue400
import com.example.rebuska.ui.theme.Blue700
import com.example.rebuska.ui.theme.Blue800
import com.example.rebuska.ui.theme.BlueBorder
import com.example.rebuska.ui.theme.DividerColor
import com.example.rebuska.ui.theme.Nunito
import com.example.rebuska.ui.theme.TextMuted
import com.example.rebuska.ui.theme.TextPrimary

@Composable
fun Registro1Screen(
    onContinuar: (nombre: String, apellido: String, email: String) -> Unit = { _, _, _ -> },
    onIniciarSesion: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var nombre   by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email    by remember { mutableStateOf("") }

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
            // HEADER CON OLA Y STEPPER
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
                Row(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 48.dp),
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    // Botón atrás
                    Box(
                        modifier = Modifier.Companion
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

                    Spacer(modifier = Modifier.Companion.width(12.dp))

                    // Stepper
                    Row(
                        modifier = Modifier.Companion.weight(1f),
                        verticalAlignment = Alignment.Companion.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Paso 1 - Activo
                        Box(
                            modifier = Modifier.Companion
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.Companion.White),
                            contentAlignment = Alignment.Companion.Center
                        ) {
                            Text(
                                text = "1",
                                fontFamily = Nunito,
                                fontWeight = FontWeight.Companion.ExtraBold,
                                fontSize = 12.sp,
                                color = Blue800
                            )
                        }
                        Spacer(modifier = Modifier.Companion.width(6.dp))
                        Text(
                            text = "Personal",
                            fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.Bold,
                            fontSize = 12.sp,
                            color = Color.Companion.White
                        )

                        // Línea conectora
                        Box(
                            modifier = Modifier.Companion
                                .padding(horizontal = 10.dp)
                                .weight(1f)
                                .height(2.dp)
                                .background(Color.Companion.White.copy(alpha = 0.3f))
                        )

                        // Paso 2 - Inactivo
                        Box(
                            modifier = Modifier.Companion
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.Companion.White.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Companion.Center
                        ) {
                            Text(
                                text = "2",
                                fontFamily = Nunito,
                                fontWeight = FontWeight.Companion.ExtraBold,
                                fontSize = 12.sp,
                                color = Color.Companion.White.copy(alpha = 0.7f)
                            )
                        }
                        Spacer(modifier = Modifier.Companion.width(6.dp))
                        Text(
                            text = "Cuenta",
                            fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.Bold,
                            fontSize = 12.sp,
                            color = Color.Companion.White.copy(alpha = 0.6f)
                        )
                    }

                    Spacer(modifier = Modifier.Companion.width(36.dp))
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
                    text = "Crea tu",
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.ExtraBold,
                    fontSize = 26.sp,
                    color = TextPrimary
                )
                Text(
                    text = "Cuenta",
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.ExtraBold,
                    fontSize = 24.sp,
                    color = Blue800
                )
                Spacer(modifier = Modifier.Companion.height(4.dp))
                Text(
                    text = "Cuéntanos quién eres",
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.SemiBold,
                    fontSize = 13.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.Companion.height(24.dp))

                // Nombre y Apellido en fila
                Row(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        placeholder = {
                            Text(
                                "Nombre", fontFamily = Nunito,
                                fontWeight = FontWeight.Companion.SemiBold, color = TextMuted
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.Companion.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Blue800,
                            unfocusedBorderColor = BlueBorder,
                            focusedContainerColor = Color.Companion.White,
                            unfocusedContainerColor = Color.Companion.White,
                            cursorColor = Blue800
                        )
                    )
                    OutlinedTextField(
                        value = apellido,
                        onValueChange = { apellido = it },
                        placeholder = {
                            Text(
                                "Apellido", fontFamily = Nunito,
                                fontWeight = FontWeight.Companion.SemiBold, color = TextMuted
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.Companion.weight(1f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Blue800,
                            unfocusedBorderColor = BlueBorder,
                            focusedContainerColor = Color.Companion.White,
                            unfocusedContainerColor = Color.Companion.White,
                            cursorColor = Blue800
                        )
                    )
                }

                Spacer(modifier = Modifier.Companion.height(14.dp))

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = {
                        Text(
                            "Correo electrónico", fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.SemiBold, color = TextMuted
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_email),
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.Companion.size(20.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Companion.Email),
                    singleLine = true,
                    modifier = Modifier.Companion.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Blue800,
                        unfocusedBorderColor = BlueBorder,
                        focusedContainerColor = Color.Companion.White,
                        unfocusedContainerColor = Color.Companion.White,
                        cursorColor = Blue800
                    )
                )

                Text(
                    text = "Recibirás un correo de verificación",
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.SemiBold,
                    fontSize = 11.sp,
                    color = TextMuted,
                    modifier = Modifier.Companion.padding(start = 4.dp, top = 4.dp)
                )

                Spacer(modifier = Modifier.Companion.weight(1f))
                Spacer(modifier = Modifier.Companion.height(24.dp))

                // Botón Continuar
                Button(
                    onClick = { onContinuar(nombre, apellido, email) },
                    modifier = Modifier.Companion.fillMaxWidth().height(52.dp),
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
                    Spacer(modifier = Modifier.Companion.width(8.dp))
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_forward),
                        contentDescription = null,
                        tint = Color.Companion.White,
                        modifier = Modifier.Companion.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.Companion.height(20.dp))

                // Divisor
                Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                    HorizontalDivider(
                        modifier = Modifier.Companion.weight(1f),
                        color = DividerColor
                    )
                    Text(
                        text = "  o regístrate con  ",
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Companion.SemiBold,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                    HorizontalDivider(
                        modifier = Modifier.Companion.weight(1f),
                        color = DividerColor
                    )
                }

                Spacer(modifier = Modifier.Companion.height(16.dp))

                // Botón Google
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.Companion.fillMaxWidth().height(52.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Companion.White,
                        contentColor = TextPrimary
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_google),
                        contentDescription = null,
                        modifier = Modifier.Companion.size(20.dp),
                        tint = Color.Companion.Unspecified
                    )
                    Spacer(modifier = Modifier.Companion.width(10.dp))
                    Text(
                        text = "Continuar con Google",
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Companion.Bold,
                        fontSize = 14.sp,
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.Companion.height(24.dp))

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
                        color = Blue800
                    )
                }

                Spacer(modifier = Modifier.Companion.height(24.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Registro1Preview() {
    Registro1Screen()
}