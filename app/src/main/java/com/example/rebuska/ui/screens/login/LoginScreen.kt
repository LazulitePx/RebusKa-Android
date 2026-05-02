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
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
fun LoginScreen(
    onLoginExitoso: (email: String, password: String) -> Unit = { _, _ -> },
    cargando: Boolean = false,
    errorMsg: String? = null,
    onRegistrarse: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

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

                            // Curva tipo ola
                            quadraticBezierTo(
                                size.width * 0.25f, size.height,
                                size.width * 0.5f, size.height * 0.85f
                            )
                            quadraticBezierTo(
                                size.width * 0.75f, size.height * 0.7f,
                                size.width, size.height * 0.85f
                            )

                            // Cerrar la forma
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


                // Botón atrás
                Box(
                    modifier = Modifier.Companion
                        .padding(start = 16.dp, top = 44.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { onBack() }
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
            // CONTENIDO BLANCO
            // ══════════════════════════════════════════
            Column(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .background(Color.Companion.White)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 28.dp)
            ) {

                Spacer(modifier = Modifier.Companion.height(20.dp))

                // Saludo
                Text(
                    text = "Hola,",
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.ExtraBold,
                    fontSize = 26.sp,
                    color = TextPrimary
                )
                Text(
                    text = "Te damos la bienvenida 👋",
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.ExtraBold,
                    fontSize = 20.sp,
                    color = Blue800
                )

                Spacer(modifier = Modifier.Companion.height(4.dp))

                Text(
                    text = "Ingresa para continuar",
                    fontFamily = Nunito,
                    fontWeight = FontWeight.Companion.SemiBold,
                    fontSize = 13.sp,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.Companion.height(28.dp))

                // Campo email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = {
                        Text(
                            "Correo electrónico",
                            fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.SemiBold,
                            color = TextMuted
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

                // Campo contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text(
                            "Contraseña",
                            fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.SemiBold,
                            color = TextMuted
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(
                                    if (passwordVisible) R.drawable.ic_visibility
                                    else R.drawable.ic_visibility_off
                                ),
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.Companion.size(20.dp)
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.Companion.None
                    else
                        PasswordVisualTransformation(),
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

                // ¿Olvidaste tu contraseña?
                Box(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    contentAlignment = Alignment.Companion.CenterEnd
                ) {
                    TextButton(onClick = {}) {
                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            fontFamily = Nunito,
                            fontWeight = FontWeight.Companion.ExtraBold,
                            fontSize = 12.sp,
                            color = Blue800
                        )
                    }
                }

                Spacer(modifier = Modifier.Companion.height(4.dp))

                // Botón Continuar
                Button(
                    onClick = { onLoginExitoso(email, password) },
                    enabled = !cargando,
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

                Spacer(modifier = Modifier.Companion.height(20.dp))

                // Divisor
                Row(
                    verticalAlignment = Alignment.Companion.CenterVertically,
                    modifier = Modifier.Companion.fillMaxWidth()
                ) {
                    HorizontalDivider(
                        modifier = Modifier.Companion.weight(1f),
                        color = DividerColor
                    )
                    Text(
                        text = "  o inicia sesión con  ",
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
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Companion.White,
                        contentColor = TextPrimary
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_google),
                        contentDescription = null,
                        modifier = Modifier.Companion.size(28.dp),
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

                Spacer(modifier = Modifier.Companion.height(28.dp))

                // ¿No tienes cuenta?
                Row(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    Text(
                        text = "¿No tienes una cuenta? ",
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Companion.SemiBold,
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "Regístrate",
                        fontFamily = Nunito,
                        fontWeight = FontWeight.Companion.ExtraBold,
                        fontSize = 13.sp,
                        color = Blue800,
                        modifier = Modifier.Companion.clickable { onRegistrarse() }
                    )
                }

                Spacer(modifier = Modifier.Companion.height(24.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}