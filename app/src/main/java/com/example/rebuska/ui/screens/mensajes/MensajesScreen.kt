package com.example.rebuska.ui.screens.mensajes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rebuska.navigation.Rutas
import com.example.rebuska.ui.components.BottomNavBar
import com.example.rebuska.ui.components.ChatItem
import com.example.rebuska.ui.components.HeaderMensajes
import com.example.rebuska.ui.components.NavDestino
import com.example.rebuska.viewmodel.ChatViewModel

@Composable
fun MensajesScreen(
    navController: NavHostController,
    viewModel: ChatViewModel = viewModel()
) {
    val chats by viewModel.chats.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarChats()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
    ) {
        HeaderMensajes()

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "RECIENTES",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(12.dp)
            )

            if (chats.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("No tienes conversaciones aún",
                        color = Color.Gray, fontSize = 14.sp)
                }
            } else {
                chats.forEach { chat ->
                    ChatItem(
                        chat = chat,
                        onClick = {
                            navController.navigate("chat/${chat.id}")
                        }
                    )
                }
            }
        }

        BottomNavBar(
            seleccionado = NavDestino.CHATS,
            onHome   = { navController.navigate(Rutas.HOME) },
            onChats  = { },
            onPerfil = { navController.navigate(Rutas.PERFIL) },
            onMenu   = { },
            onLogo   = { navController.navigate(Rutas.HOME) }
        )
    }
}