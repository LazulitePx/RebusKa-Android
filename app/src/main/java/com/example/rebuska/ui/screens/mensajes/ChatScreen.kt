package com.example.rebuska.ui.screens.mensajes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rebuska.data.model.Mensaje
import com.example.rebuska.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding

@Composable
fun ChatScreen(
    chatId: String,
    onBack: () -> Unit,
    viewModel: ChatViewModel = viewModel()
) {
    val mensajes by viewModel.mensajes.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val listState = rememberLazyListState()
    var texto by remember { mutableStateOf("") }

    LaunchedEffect(chatId) {
        viewModel.abrirChat(chatId)
    }

    LaunchedEffect(mensajes.size) {
        if (mensajes.isNotEmpty()) {
            listState.animateScrollToItem(mensajes.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
    ) {
        // ── Header
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .background(Color(0xFF1976D2))
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1976D2))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
            }
            Spacer(Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text("💬", fontSize = 20.sp)
            }
            Spacer(Modifier.width(10.dp))
            Text("Chat", color = Color.White, fontSize = 18.sp,
                fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        }

        // ── Mensajes
        if (cargando) {
            Box(Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF1976D2))
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(mensajes) { mensaje ->
                    MensajeBurbuja(
                        mensaje = mensaje,
                        esMio   = mensaje.idEmisor == viewModel.uidActual
                    )
                }
            }
        }

        // ── Input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5))
                .navigationBarsPadding()
                .imePadding()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = texto,
                onValueChange = { texto = it },
                modifier = Modifier.weight(1f).clip(RoundedCornerShape(50)),
                placeholder = { Text("Escribe un mensaje") },
                shape = RoundedCornerShape(50),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor   = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor   = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = {
                if (texto.isNotBlank()) {
                    viewModel.enviarMensaje(texto)
                    texto = ""
                }
            }) {
                Icon(Icons.AutoMirrored.Filled.Send, null, tint = Color(0xFF1976D2))
            }
        }
    }
}

@Composable
fun MensajeBurbuja(mensaje: Mensaje, esMio: Boolean) {
    val hora = SimpleDateFormat("h:mm a", Locale.getDefault())
        .format(Date(mensaje.timestamp))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (esMio) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (esMio) Color(0xFF1976D2) else Color.White,
                    RoundedCornerShape(12.dp)
                )
                .padding(10.dp)
        ) {
            Text(mensaje.texto, color = if (esMio) Color.White else Color.Black)
        }
        Text(hora, fontSize = 10.sp, color = Color.Gray)
    }
}