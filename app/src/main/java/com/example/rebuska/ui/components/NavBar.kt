package com.example.rebuska.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rebuska.R
import com.example.rebuska.ui.theme.*

enum class NavDestino { HOME, CHATS, PERFIL, MENU }

@Composable
fun BottomNavBar(
    seleccionado: NavDestino = NavDestino.HOME,
    onHome: () -> Unit = {},
    onChats: () -> Unit = {},
    onLogo: () -> Unit = {},
    onPerfil: () -> Unit = {},
    onMenu: () -> Unit = {}
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp,

        ) {
        NavigationBarItem(
            selected = seleccionado == NavDestino.HOME,
            onClick = onHome,
            icon = { Icon(painterResource(R.drawable.ic_home), null, Modifier.size(20.dp)) },
            label = { Text("Home", fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 9.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Blue800, selectedTextColor = Blue800,
                unselectedIconColor = Color(0xFFAAAAAA), indicatorColor = BlueLight
            )
        )
        NavigationBarItem(
            selected = seleccionado == NavDestino.CHATS,
            onClick = onChats,
            icon = { Icon(painterResource(R.drawable.ic_chat), null, Modifier.size(20.dp)) },
            label = { Text("Chats", fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 9.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Blue800, selectedTextColor = Blue800,
                unselectedIconColor = Color(0xFFAAAAAA), indicatorColor = BlueLight
            )
        )
        // Logo central
        NavigationBarItem(
            selected = false,
            onClick = onLogo,
            icon = {
                Box(
                    modifier = Modifier.size(70.dp)
                        .offset(y = 6.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painterResource(R.drawable.logo), null, Modifier.size(50.dp), tint = Color.Unspecified)
                }
            },
            label = { Text("") },
            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
        )
        // Perfil
        NavigationBarItem(
            selected = seleccionado == NavDestino.PERFIL,
            onClick = onPerfil,
            icon = { Icon(painterResource(R.drawable.ic_person), null, Modifier.size(20.dp)) },
            label = { Text("Perfil", fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 9.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Blue800, selectedTextColor = Blue800,
                unselectedIconColor = Color(0xFFAAAAAA), indicatorColor = BlueLight
            )
        )

        NavigationBarItem(
            selected = seleccionado == NavDestino.MENU,
            onClick = onMenu,
            icon = { Icon(painterResource(R.drawable.ic_menu), null, Modifier.size(20.dp)) },
            label = { Text("Menú", fontFamily = Nunito, fontWeight = FontWeight.Bold, fontSize = 9.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Blue800, selectedTextColor = Blue800,
                unselectedIconColor = Color(0xFFAAAAAA), indicatorColor = BlueLight
            )
        )
    }
}