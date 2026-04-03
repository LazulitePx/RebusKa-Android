package com.example.rebuska.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rebuska.R
import com.example.rebuska.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit = {}
) {
    // ── Animaciones ───────────────────────────────────
    val logoScale      = remember { Animatable(0.6f) }
    val contentAlpha   = remember { Animatable(0f) }
    val contentOffset  = remember { Animatable(24f) }
    val loaderProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
        contentAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500)
        )
        contentOffset.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 500)
        )
        loaderProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1800, easing = FastOutSlowInEasing)
        )
        delay(200)
        onSplashFinished()
    }

    // ── UI ────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF3A7DDE),
                        Color(0xFF389AED),
                        Color(0xFF2E70D0)
                    ),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end   = androidx.compose.ui.geometry.Offset(400f, 1200f)
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        // ── Círculos decorativos ──────────────────────
        Box(
            modifier = Modifier
                .size(320.dp)
                .offset(x = 80.dp, y = (-140).dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White.copy(alpha = 0.05f))
                .align(Alignment.TopEnd)
        )
        Box(
            modifier = Modifier
                .size(220.dp)
                .offset(x = (-60).dp, y = 60.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White.copy(alpha = 0.05f))
                .align(Alignment.BottomStart)
        )
        Box(
            modifier = Modifier
                .size(90.dp)
                .offset(x = 260.dp, y = (-200).dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White.copy(alpha = 0.05f))
                .align(Alignment.BottomStart)
        )

        // ── Contenido central ─────────────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            // Logo grande con pop-in
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Rebuska",
                modifier = Modifier
                    .size(240.dp)
                    .scale(logoScale.value)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Slogan
            Text(
                text = "Conecta · Trabaja · Crece",
                fontFamily = Nunito,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.72f),
                textAlign = TextAlign.Center,
                letterSpacing = 0.3.sp,
                modifier = Modifier
                    .offset(y = contentOffset.value.dp)
                    .graphicsLayer { alpha = contentAlpha.value }
            )
        }

        // ── Barra de carga inferior ───────────────────
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 72.dp)
                    .graphicsLayer { alpha = contentAlpha.value }
            ) {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(3.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = loaderProgress.value)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}