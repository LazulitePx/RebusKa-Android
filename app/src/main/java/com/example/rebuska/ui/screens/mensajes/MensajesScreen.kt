package com.example.rebuska.ui.screens.mensajes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import java.text.Normalizer

@Composable
fun MensajesScreen(
    navController: NavHostController,
    viewModel: ChatViewModel = viewModel()
) {
    val chats by viewModel.chats.collectAsState()
    val uidActual = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val esTrabajador = chats.any { it.idUsuario2 == uidActual }

    // Estados de filtrado y búsqueda
    var filtroSeleccionado by remember { mutableStateOf("Todos") }
    var searchText by remember { mutableStateOf("") }

    // Obtener nombres unicos de tiendas del trabajador
    val tiendas = if (esTrabajador) {
        chats.map { it.nombreNegocio }.distinct().filter { it.isNotEmpty() }
    } else emptyList()

    // Chats filtrados por tienda y texto de búsqueda (ignorando tildes y mayúsculas)
    val chatsFiltrados = remember(chats, filtroSeleccionado, searchText) {
        val queryNormalizada = searchText.removerAcentos().lowercase()
        
        chats.filter { chat ->
            val cumpleFiltroTienda = !esTrabajador || filtroSeleccionado == "Todos" || chat.nombreNegocio == filtroSeleccionado
            
            val nombreContactoNormalizado = chat.nombreContacto.removerAcentos().lowercase()
            val ultimoMensajeNormalizado = chat.ultimoMensaje.removerAcentos().lowercase()
            val nombreNegocioNormalizado = chat.nombreNegocio.removerAcentos().lowercase()

            val cumpleBusqueda = nombreContactoNormalizado.contains(queryNormalizada) ||
                                ultimoMensajeNormalizado.contains(queryNormalizada) ||
                                nombreNegocioNormalizado.contains(queryNormalizada)
            
            cumpleFiltroTienda && cumpleBusqueda
        }
    }

    LaunchedEffect(Unit) {
        viewModel.cargarChats()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
    ) {
        HeaderMensajes(
            searchText = searchText,
            onSearchTextChange = { searchText = it }
        )

        // ── Filtros por tienda (solo trabajador)
        if (esTrabajador && tiendas.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FiltroChip(
                        texto      = "Todos",
                        seleccionado = filtroSeleccionado == "Todos",
                        onClick    = { filtroSeleccionado = "Todos" }
                    )
                }
                items(tiendas) { tienda ->
                    FiltroChip(
                        texto        = tienda,
                        seleccionado = filtroSeleccionado == tienda,
                        onClick      = { filtroSeleccionado = tienda }
                    )
                }
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "RECIENTES",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(12.dp)
            )

            if (chatsFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (searchText.isEmpty()) "No tienes conversaciones aún" else "No se encontraron resultados",
                        color = Color.Gray, 
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(chatsFiltrados) { chat ->
                        val esCliente = uidActual == chat.idUsuario1
                        ChatItem(
                            chat    = chat,
                            onClick = {
                                navController.navigate(
                                    "chat/${chat.id}/${
                                        java.net.URLEncoder.encode(chat.nombreContacto, "UTF-8")
                                    }/${
                                        java.net.URLEncoder.encode(chat.fotoUrl, "UTF-8")
                                    }/$esCliente"
                                )
                            }
                        )
                    }
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

// Extensión para normalizar texto eliminando acentos/tildes
fun String.removerAcentos(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return "\\p{InCombiningDiacriticalMarks}+".toRegex().replace(temp, "")
}

@Composable
fun FiltroChip(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                if (seleccionado) Color(0xFF1976D2) else Color(0xFFF2F2F2),
                RoundedCornerShape(50.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            fontSize = 13.sp,
            fontWeight = if (seleccionado) FontWeight.ExtraBold else FontWeight.Normal,
            color = if (seleccionado) Color.White else Color.Gray
        )
    }
}