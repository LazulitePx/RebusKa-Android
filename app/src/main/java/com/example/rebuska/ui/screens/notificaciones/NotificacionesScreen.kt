package com.example.rebuska.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rebuska.data.model.Chat
import com.example.rebuska.ui.theme.Blue400
import com.example.rebuska.ui.theme.Blue700
import com.example.rebuska.ui.theme.Blue800
import com.example.rebuska.ui.theme.Nunito
import com.example.rebuska.viewmodel.ChatViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NotificacionesScreen(
    onBack: () -> Unit = {},
    onVerChat: (chatId: String, nombre: String, logo: String, esCliente: Boolean) -> Unit = { _, _, _, _ -> },
    viewModel: ChatViewModel = viewModel()
) {
    val chats by viewModel.chats.collectAsState()
    val uidActual = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val chatsConMensajes = chats.filter { it.noLeidos > 0 }

    LaunchedEffect(Unit) {
        viewModel.cargarChats()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F4F8))
    ) {
        // ── Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(Blue800, Blue700, Blue400),
                        androidx.compose.ui.geometry.Offset(0f, 0f),
                        androidx.compose.ui.geometry.Offset(400f, 200f)
                    )
                )
                .padding(start = 12.dp, end = 12.dp, top = 44.dp, bottom = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.18f))
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack, null,
                        tint = Color.White, modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    "Notificaciones", fontFamily = Nunito,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp, color = Color.White,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (chatsConMensajes.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = null,
                                tint = Color.LightGray,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "No tienes notificaciones nuevas",
                                fontFamily = Nunito,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            } else {
                item {
                    Text(
                        "NUEVOS MENSAJES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                    )
                }

                items(chatsConMensajes) { chat ->
                    val esCliente = uidActual == chat.idUsuario1
                    NotificacionItem(
                        chat      = chat,
                        esCliente = esCliente,
                        onClick   = {
                            onVerChat(chat.id, chat.nombreContacto, chat.fotoUrl, esCliente)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificacionItem(
    chat: Chat,
    esCliente: Boolean,
    onClick: () -> Unit
) {
    val hora = if (chat.timestamp > 0L)
        SimpleDateFormat("dd/MM · h:mm a", Locale.getDefault()).format(Date(chat.timestamp))
    else ""

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color(0xFFE3F2FD)),
            contentAlignment = Alignment.Center
        ) {
            if (esCliente && chat.fotoUrl.isNotEmpty()) {
                coil.compose.AsyncImage(
                    model = chat.fotoUrl,
                    contentDescription = null,
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier.size(50.dp).clip(CircleShape)
                )
            } else {
                val inicial = chat.nombreContacto.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
                Text(
                    inicial, fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blue800
                )
            }
        }

        // Contenido
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    chat.nombreContacto,
                    fontFamily = Nunito,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color(0xFF1A1A2E)
                )
                Text(
                    hora, fontSize = 10.sp, color = Color.Gray
                )
            }

            if (!esCliente && chat.nombreNegocio.isNotEmpty()) {
                Text(
                    "vía ${chat.nombreNegocio}",
                    fontSize = 11.sp,
                    color = Blue800,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(3.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    chat.ultimoMensaje,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                // Badge de no leídos
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Blue800, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (chat.noLeidos > 9) "9+" else "${chat.noLeidos}",
                        fontSize = 10.sp,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}