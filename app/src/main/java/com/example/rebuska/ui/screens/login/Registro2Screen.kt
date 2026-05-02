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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
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
fun Registro2Screen(
    onCrearCuenta: (cedula: String, celular: String, password: String) -> Unit = { _, _, _ -> },
    cargando: Boolean = false,
    errorMsg: String? = null,
    onIniciarSesion: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var cedula            by remember { mutableStateOf("") }
    var celular           by remember { mutableStateOf("") }
    var password          by remember { mutableStateOf("") }
    var confirmarPassword by remember { mutableStateOf("") }
    var passVisible       by remember { mutableStateOf(false) }
    var confirmVisible    by remember { mutableStateOf(false) }

    // Fuerza de contraseña
    val passwordStrength = when {
        password.length >= 10 && password.any { it.isUpperCase() } &&
                password.any { it.isDigit() } -> 4
        password.length >= 8  -> 3
        password.length >= 6  -> 2
        password.isNotEmpty() -> 1
        else -> 0
    }
    val strengthLabel = when (passwordStrength) {
        1 -> "Muy débil"
        2 -> "Débil"
        3 -> "Media"
        4 -> "Fuerte"
        else -> ""
    }
    val strengthColor = when (passwordStrength) {
        1 -> Color(0xFFC62828)
        2 -> Color(0xFFF57F17)
        3 -> Color(0xFFEAB308)
        4 -> Color(0xFF2E7D32)
        else -> DividerColor
    }

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
                        // Paso 1 - Completado
                        Box(
                            modifier = Modifier.Companion
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.Companion.White),
                            contentAlignment = Alignment.Companion.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = null,
                                tint = Blue800,
                                modifier = Modifier.Companion.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.Companion.width(6.dp))
                        Text(
                            text = "Personal",
                            fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.Bold,
                            fontSize = 12.sp,
                            color = Color.Companion.White.copy(alpha = 0.85f)
                        )

                        // Línea conectora completada
                        Box(
                            modifier = Modifier.Companion
                                .padding(horizontal = 10.dp)
                                .weight(1f)
                                .height(2.dp)
                                .background(Color.Companion.White.copy(alpha = 0.8f))
                        )

                        // Paso 2 - Activo
                        Box(
                            modifier = Modifier.Companion
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.Companion.White),
                            contentAlignment = Alignment.Companion.Center
                        ) {
                            Text(
                                text = "2",
                                fontFamily = Nunito,
                                fontWeight = FontWeight.Companion.ExtraBold,
                                fontSize = 12.sp,
                                color = Blue800
                            )
                        }
                        Spacer(modifier = Modifier.Companion.width(6.dp))
                        Text(
                            text = "Cuenta",
                            fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.Bold,
                            fontSize = 12.sp,
                            color = Color.Companion.White
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
                    text = "Casi",
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.ExtraBold,
                    fontSize = 26.sp,
                    color = TextPrimary
                )
                Text(
                    text = "Terminamos",
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.ExtraBold,
                    fontSize = 24.sp,
                    color = Blue800
                )
                Spacer(modifier = Modifier.Companion.height(4.dp))
                Text(
                    text = "Solo un paso más...",
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.SemiBold,
                    fontSize = 13.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.Companion.height(24.dp))

                // Cédula
                OutlinedTextField(
                    value = cedula,
                    onValueChange = { cedula = it },
                    placeholder = {
                        Text(
                            "Número de cédula", fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.SemiBold, color = TextMuted
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_id_card),
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.Companion.size(20.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Companion.Number),
                    singleLine = true,
                    modifier = Modifier.Companion.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Blue800,
                        unfocusedBorderColor = BlueBorder,
                        focusedContainerColor = Color.Companion.White,
                        unfocusedContainerColor = Color.Companion.White,
                        cursorColor = Blue800
                    )
                )

                Spacer(modifier = Modifier.Companion.height(14.dp))

                // Celular con prefijo
                OutlinedTextField(
                    value = celular,
                    onValueChange = { celular = it },
                    placeholder = {
                        Text(
                            "Número de celular", fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.SemiBold, color = TextMuted
                        )
                    },
                    leadingIcon = {
                        Text(
                            text = "🇨🇴 +57",
                            fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.Bold,
                            fontSize = 13.sp,
                            color = TextPrimary,
                            modifier = Modifier.Companion.padding(start = 4.dp)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_phone),
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.Companion.size(20.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Companion.Phone),
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
                    text = "Lo usaremos para verificar tu cuenta",
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.SemiBold,
                    fontSize = 11.sp,
                    color = TextMuted,
                    modifier = Modifier.Companion.padding(start = 4.dp, top = 4.dp)
                )

                Spacer(modifier = Modifier.Companion.height(14.dp))

                // Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text(
                            "Contraseña", fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.SemiBold, color = TextMuted
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passVisible = !passVisible }) {
                            Icon(
                                painter = painterResource(
                                    if (passVisible) R.drawable.ic_visibility
                                    else R.drawable.ic_visibility_off
                                ),
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.Companion.size(20.dp)
                            )
                        }
                    },
                    visualTransformation = if (passVisible) VisualTransformation.Companion.None
                    else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Companion.Password),
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

                Spacer(modifier = Modifier.Companion.height(14.dp))

                // Confirmar contraseña
                OutlinedTextField(
                    value = confirmarPassword,
                    onValueChange = { confirmarPassword = it },
                    placeholder = {
                        Text(
                            "Confirmar contraseña", fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.SemiBold, color = TextMuted
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { confirmVisible = !confirmVisible }) {
                            Icon(
                                painter = painterResource(
                                    if (confirmVisible) R.drawable.ic_visibility
                                    else R.drawable.ic_visibility_off
                                ),
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.Companion.size(20.dp)
                            )
                        }
                    },
                    visualTransformation = if (confirmVisible) VisualTransformation.Companion.None
                    else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Companion.Password),
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

                // Barra de fortaleza
                if (password.isNotEmpty()) {
                    Spacer(modifier = Modifier.Companion.height(8.dp))
                    Row(
                        modifier = Modifier.Companion.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(4) { index ->
                            Box(
                                modifier = Modifier.Companion
                                    .weight(1f)
                                    .height(3.dp)
                                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(2.dp))
                                    .background(
                                        if (index < passwordStrength) strengthColor
                                        else DividerColor
                                    )
                            )
                        }
                    }
                    Text(
                        text = strengthLabel,
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Companion.Bold,
                        fontSize = 11.sp,
                        color = strengthColor,
                        modifier = Modifier.Companion
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        textAlign = TextAlign.End
                    )
                }

                Spacer(modifier = Modifier.Companion.height(28.dp))

                // Botón Crear cuenta (verde)
                Button(
                    onClick = {
                        onCrearCuenta(cedula, celular, password)
                    },
                    enabled = !cargando,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = null,
                        tint = Color.Companion.White,
                        modifier = Modifier.Companion.size(18.dp)
                    )
                    Spacer(modifier = Modifier.Companion.width(8.dp))
                    Text(
                        text = "Crear cuenta",
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Companion.ExtraBold,
                        fontSize = 15.sp,
                        color = Color.Companion.White
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
fun Registro2Preview() {
    Registro2Screen()
}