package com.example.rebuska.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rebuska.ui.screens.LoginScreen
import com.example.rebuska.ui.screens.RegistroRolScreen
import com.example.rebuska.ui.screens.SplashScreen
import com.example.rebuska.ui.screens.Registro1Screen
import com.example.rebuska.ui.screens.Registro2Screen
import com.example.rebuska.ui.screens.VerificacionEmailScreen
import com.example.rebuska.ui.screens.VerificacionTelefonoScreen
import com.example.rebuska.ui.screens.HomeScreen
import com.example.rebuska.ui.screens.TiendaScreen
import com.example.rebuska.ui.screens.mensajes.ChatScreen
import com.example.rebuska.ui.screens.mensajes.MensajesScreen

object Rutas {
    const val SPLASH               = "splash"
    const val LOGIN                = "login"
    const val REGISTRO_ROL         = "registro_rol"
    const val HOME                 = "home"
    const val REGISTRO_1           = "registro_1"
    const val REGISTRO_2           = "registro_2"
    const val VERIFICACION_EMAIL   = "verificacion_email"
    const val VERIFICACION_TELEFONO = "verificacion_telefono"
    const val TIENDA = "tienda/{idNegocio}"
    const val MENSAJES = "mensajes"
    const val CHAT = "chat/{nombre}"


    // Función helper
    fun tiendaRuta(id: Int) = "tienda/$id"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController    = navController,
        startDestination = Rutas.SPLASH
    ) {

        composable(Rutas.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.LOGIN) {
            LoginScreen(
                onLoginExitoso = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                },
                onRegistrarse = {
                    navController.navigate(Rutas.REGISTRO_ROL)
                }
            )
        }

        composable(Rutas.REGISTRO_ROL) {
            RegistroRolScreen(
                onContinuar = {
                    navController.navigate(Rutas.REGISTRO_1) {
                        popUpTo(Rutas.REGISTRO_ROL) { inclusive = true }
                    }
                },
                onIniciarSesion = {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.REGISTRO_ROL) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Rutas.REGISTRO_1) {
            Registro1Screen(
                onContinuar = { _, _, _ ->
                    navController.navigate(Rutas.REGISTRO_2)
                },
                onIniciarSesion = {
                    navController.navigate(Rutas.LOGIN)
                },
                onBack = { navController.navigate(Rutas.REGISTRO_ROL) }
            )
        }

        composable(Rutas.REGISTRO_2) {
            Registro2Screen(
                onCrearCuenta = {
                    navController.navigate(Rutas.VERIFICACION_EMAIL) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                },
                onIniciarSesion = {
                    navController.navigate(Rutas.LOGIN)
                },
                onBack = { navController.navigate(Rutas.REGISTRO_1) }
            )
        }

        composable(Rutas.VERIFICACION_EMAIL) {
            VerificacionEmailScreen(
                onVerificar = {
                    navController.navigate(Rutas.VERIFICACION_TELEFONO)
                }
            )
        }

        composable(Rutas.VERIFICACION_TELEFONO) {
            VerificacionTelefonoScreen(
                onVerificar = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.HOME) {
            HomeScreen(
                onVerTienda = { id ->
                    // Navega a tienda/1, tienda/2, tienda/3 según el negocio tocado
                    navController.navigate(Rutas.tiendaRuta(id))
                },
                onLogin = {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.HOME) { inclusive = true }
                    }
                },
                onChats = {
                    navController.navigate(Rutas.MENSAJES)

                }
            )
        }

        composable(
            route = Rutas.TIENDA,
            arguments = listOf(
                navArgument("idNegocio") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            // Extrae el ID de la URL
            val idNegocio = backStackEntry.arguments?.getInt("idNegocio") ?: 1
            TiendaScreen(
                idNegocio = idNegocio,
                onAtras   = { navController.popBackStack() },
                onHome    = { navController.navigate(Rutas.HOME) },
                onChats   = { navController.navigate(Rutas.LOGIN) },
                onPerfil  = { navController.navigate(Rutas.LOGIN) }
            )
        }
        composable(Rutas.MENSAJES) {
            MensajesScreen(navController)
        }

        composable(
            route = Rutas.CHAT,
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""

            ChatScreen(
                nombre = nombre,
                onBack = { navController.popBackStack() }
            )
        }

    }
}