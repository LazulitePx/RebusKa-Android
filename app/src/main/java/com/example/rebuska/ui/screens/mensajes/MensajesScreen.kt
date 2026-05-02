package com.example.rebuska.ui.screens.mensajes


    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.*
    import androidx.compose.material3.*
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.example.rebuska.ui.components.HeaderMensajes
    import com.example.rebuska.data.model.Chat
    import com.example.rebuska.ui.components.ChatItem
    import androidx.lifecycle.viewmodel.compose.viewModel
    import androidx.navigation.NavHostController
    import com.example.rebuska.R
    import com.example.rebuska.navigation.Rutas
    import com.example.rebuska.ui.components.BottomNavBar
    import com.example.rebuska.ui.components.NavDestino
    import com.example.rebuska.viewmodel.MensajesViewModel

@Composable
fun MensajesScreen(
    navController: NavHostController,
    viewModel: MensajesViewModel = viewModel()
) {


    val chats = viewModel.chats


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

            chats.forEach { chat ->
                ChatItem(
                    chat = chat,
                    onClick = {
                        if (chat.nombre == "Carpintería López") {
                            navController.navigate("chat/${chat.nombre}")
                        }
                    }
                )
            }
        }

        BottomNavBar(
            seleccionado = NavDestino.CHATS,

            onHome = {
                navController.navigate(Rutas.HOME)
            },

            onChats = {
                //aqui
            },

            onPerfil = {
                navController.navigate("perfil")
            },

            onMenu = {
                navController.navigate("menu")
            },

            onLogo = {
                navController.navigate(Rutas.HOME)
            }
        )
    }
}