package com.example.rebuska.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rebuska.data.model.Chat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatItem(
    chat: Chat,
    onClick: () -> Unit
) {
    val uidActual = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val esCliente = uidActual == chat.idUsuario1
    val inicial = chat.nombreContacto.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ── Avatar
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color(0xFFE0E0E0), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (esCliente && chat.fotoUrl.isNotEmpty()) {
                coil.compose.AsyncImage(
                    model = chat.fotoUrl,
                    contentDescription = null,
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
            } else {
                Text(
                    text = inicial,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // ── Nombre y último mensaje
        Column(modifier = Modifier.weight(1f)) {
            Text(
                chat.nombreContacto,
                fontWeight = if (chat.noLeidos > 0) FontWeight.ExtraBold else FontWeight.Bold
            )
            Text(
                chat.ultimoMensaje,
                fontSize = 12.sp,
                color = if (chat.noLeidos > 0) Color(0xFF1976D2) else Color.Gray,
                fontWeight = if (chat.noLeidos > 0) FontWeight.SemiBold else FontWeight.Normal,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // ── Hora y contador
        Column(horizontalAlignment = Alignment.End) {
            val hora = if (chat.timestamp > 0L) {
                SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(chat.timestamp))
            } else ""
            Text(
                hora,
                fontSize = 10.sp,
                color = if (chat.noLeidos > 0) Color(0xFF1976D2) else Color.Gray
            )

            if (chat.noLeidos > 0) {
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color(0xFF1976D2), CircleShape),
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