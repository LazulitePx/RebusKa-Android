package com.example.rebuska.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rebuska.ui.screens.HomeScreen
import com.example.rebuska.ui.screens.SplashScreen
import com.example.rebuska.ui.screens.TiendaScreen
import com.example.rebuska.ui.screens.login.LoginScreen
import com.example.rebuska.ui.screens.login.Registro1Screen
import com.example.rebuska.ui.screens.login.Registro2Screen
import com.example.rebuska.ui.screens.login.RegistroRolScreen
import com.example.rebuska.ui.screens.mensajes.ChatScreen
import com.example.rebuska.ui.screens.mensajes.MensajesScreen
import com.example.rebuska.ui.screens.perfil.ProfileScreen
import com.example.rebuska.ui.screens.perfil.ProfileScreenEdit
import com.example.rebuska.ui.screens.verificacion.VerificacionEmailScreen
import com.example.rebuska.ui.screens.verificacion.VerificacionTelefonoScreen
import com.example.rebuska.ui.viewmodel.LoginViewModel
import com.example.rebuska.ui.viewmodel.RegistroViewModel

object Rutas {
    const val SPLASH                = "splash"
    const val LOGIN                 = "login"
    const val REGISTRO_ROL          = "registro_rol"
    const val REGISTRO_1            = "registro_1"
    const val REGISTRO_2            = "registro_2"
    // ← El teléfono viaja como argumento desde registro hasta la verificación SMS
    const val VERIFICACION_EMAIL    = "verificacion_email/{telefono}"
    const val VERIFICACION_TELEFONO = "verificacion_telefono/{telefono}"
    const val HOME                  = "home"
    const val TIENDA                = "tienda/{idNegocio}"
    const val MENSAJES              = "mensajes"
    const val CHAT                  = "chat/{nombre}"
    const val PERFIL                = "perfil"
    const val NEGOCIO               = "negocio/{id}"

    // ── Helpers ───────────────────────────────────────
    fun verificacionEmailRuta(telefono: String)    = "verificacion_email/$telefono"
    fun verificacionTelefonoRuta(telefono: String) = "verificacion_telefono/$telefono"
    fun tiendaRuta(id: String)  = "tienda/$id"
    fun negocioRuta(id: String) = "negocio/$id"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController    = navController,
        startDestination = Rutas.SPLASH
    ) {

        // ── SPLASH ────────────────────────────────────────
        composable(Rutas.SPLASH) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // ── LOGIN ─────────────────────────────────────────
        composable(Rutas.LOGIN) {
            val viewModel: LoginViewModel = viewModel()
            val cargando by viewModel.cargando.observeAsState(false)
            val errorMsg by viewModel.error.observeAsState(null)
            val exitoso  by viewModel.loginExitoso.observeAsState(false)

            LaunchedEffect(exitoso) {
                if (exitoso) navController.navigate(Rutas.HOME) {
                    popUpTo(Rutas.LOGIN) { inclusive = true }
                }
            }

            LoginScreen(
                onLoginExitoso = { email, password -> viewModel.iniciarSesion(email, password) },
                cargando       = cargando,
                errorMsg       = errorMsg,
                onRegistrarse  = { navController.navigate(Rutas.REGISTRO_ROL) },
                onBack         = { navController.popBackStack() }
            )
        }

        // ── REGISTRO ROL ──────────────────────────────────
        composable(Rutas.REGISTRO_ROL) { entry ->
            val viewModel: RegistroViewModel = viewModel(entry)
            RegistroRolScreen(
                onContinuar = { rol ->
                    viewModel.rol = rol
                    navController.navigate(Rutas.REGISTRO_1)
                },
                onIniciarSesion = {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.REGISTRO_ROL) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ── REGISTRO PASO 1 ───────────────────────────────
        composable(Rutas.REGISTRO_1) { entry ->
            val parentEntry = remember(entry) {
                navController.getBackStackEntry(Rutas.REGISTRO_ROL)
            }
            val viewModel: RegistroViewModel = viewModel(parentEntry)
            Registro1Screen(
                onContinuar = { nombre, apellido, email ->
                    viewModel.nombre   = nombre
                    viewModel.apellido = apellido
                    viewModel.email    = email
                    navController.navigate(Rutas.REGISTRO_2)
                },
                onIniciarSesion = { navController.navigate(Rutas.LOGIN) },
                onBack          = { navController.popBackStack() }
            )
        }

        // ── REGISTRO PASO 2 ───────────────────────────────
        composable(Rutas.REGISTRO_2) { entry ->
            val parentEntry = remember(entry) {
                navController.getBackStackEntry(Rutas.REGISTRO_ROL)
            }
            val viewModel: RegistroViewModel = viewModel(parentEntry)
            val cargando by viewModel.cargando.observeAsState(false)
            val errorMsg by viewModel.error.observeAsState(null)
            val exitoso  by viewModel.registroExitoso.observeAsState(false)

            LaunchedEffect(exitoso) {
                if (exitoso) {
                    // ← Pasa el teléfono a la pantalla de verificación email
                    navController.navigate(Rutas.verificacionEmailRuta(viewModel.telefono)) {
                        popUpTo(Rutas.REGISTRO_ROL) { inclusive = true }
                    }
                }
            }

            Registro2Screen(
                onCrearCuenta = { cedula, celular, password ->
                    viewModel.cedula   = cedula
                    viewModel.telefono = celular
                    viewModel.password = password
                    viewModel.registrar()
                },
                cargando        = cargando,
                errorMsg        = errorMsg,
                onIniciarSesion = { navController.navigate(Rutas.LOGIN) },
                onBack          = { navController.popBackStack() }
            )
        }

        // ── VERIFICACIÓN EMAIL ────────────────────────────
        // Recibe el teléfono para pasárselo a la siguiente pantalla
        composable(
            route     = Rutas.VERIFICACION_EMAIL,
            arguments = listOf(navArgument("telefono") { type = NavType.StringType })
        ) { backStackEntry ->
            val telefono = backStackEntry.arguments?.getString("telefono") ?: ""
            VerificacionEmailScreen(
                onVerificar = {
                    // ← Pasa el teléfono a la verificación SMS
                    navController.navigate(Rutas.verificacionTelefonoRuta(telefono))
                }
            )
        }

        // ── VERIFICACIÓN TELÉFONO ─────────────────────────
        composable(
            route     = Rutas.VERIFICACION_TELEFONO,
            arguments = listOf(navArgument("telefono") { type = NavType.StringType })
        ) { backStackEntry ->
            val telefono = backStackEntry.arguments?.getString("telefono") ?: ""
            VerificacionTelefonoScreen(
                telefono    = telefono,
                onVerificado = {
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // ── HOME ──────────────────────────────────────────
        composable(Rutas.HOME) {
            HomeScreen(
                onVerTienda = { id -> navController.navigate(Rutas.tiendaRuta(id)) },
                onLogin     = {
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(Rutas.HOME) { inclusive = true }
                    }
                },
                onChats  = { navController.navigate(Rutas.MENSAJES) },
                onPerfil = { navController.navigate(Rutas.PERFIL) }
            )
        }

        // ── TIENDA ────────────────────────────────────────
        composable(
            route     = Rutas.TIENDA,
            arguments = listOf(navArgument("idNegocio") { type = NavType.StringType })
        ) { backStackEntry ->
            val idNegocio = backStackEntry.arguments?.getString("idNegocio") ?: "1"
            TiendaScreen(
                idNegocio = idNegocio,
                onAtras   = { navController.popBackStack() },
                onHome    = { navController.navigate(Rutas.HOME) },
                onChats   = { navController.navigate(Rutas.MENSAJES) },
                onPerfil  = { navController.navigate(Rutas.PERFIL) }
            )
        }

        // ── MENSAJES ──────────────────────────────────────
        composable(Rutas.MENSAJES) {
            MensajesScreen(navController)
        }

        // ── CHAT ──────────────────────────────────────────
        composable(
            route     = Rutas.CHAT,
            arguments = listOf(navArgument("nombre") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            ChatScreen(nombre = nombre, onBack = { navController.popBackStack() })
        }

        // ── PERFIL ────────────────────────────────────────
        composable(Rutas.PERFIL) {
            ProfileScreen(navController)
        }

        // ── NEGOCIO ───────────────────────────────────────
        composable(
            route     = Rutas.NEGOCIO,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val negocioId = backStackEntry.arguments?.getInt("id") ?: 0
            ProfileScreenEdit(navController, negocioId)
        }
    }
}