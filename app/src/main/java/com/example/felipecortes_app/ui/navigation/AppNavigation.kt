package com.example.felipecortes_app.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.felipecortes_app.ui.screens.DetalleArriendoScreen
import com.example.felipecortes_app.ui.screens.InformeScreen
import com.example.felipecortes_app.ui.screens.InicioScreen
import com.example.felipecortes_app.ui.screens.LoginScreen
import com.example.felipecortes_app.ui.screens.MisReservasScreen
import com.example.felipecortes_app.ui.screens.MovimientosScreen
import com.example.felipecortes_app.ui.screens.PostScreen
import com.example.felipecortes_app.ui.screens.RecuperarContrasenaScreen
import com.example.felipecortes_app.ui.screens.RegistroScreen
import com.example.felipecortes_app.ui.screens.ReporteScreen
import com.example.felipecortes_app.ui.screens.ResumenScreen
import com.example.felipecortes_app.ui.screens.RevisarSolicitudesScreen
import com.example.felipecortes_app.viewmodel.AuthViewModel
import com.example.felipecortes_app.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation(anchoDisponible: WindowWidthSizeClass) {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val usuarioLogeado by authViewModel.usuarioActual.collectAsState()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginExitoso = {
                    navController.navigate("inicio") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onOlvideContrasena = { navController.navigate("recuperar") }
            )
        }
        composable("recuperar") {
            RecuperarContrasenaScreen(
                authViewModel = authViewModel,
                onVolver = { navController.popBackStack() }
            )
        }
        composable("inicio") {
            usuarioLogeado?.let { usuario ->
                InicioScreen(
                    anchoDisponible = anchoDisponible,
                    nombreUsuario = usuario.nombre,
                    rol = usuario.rol,
                    onVerDetalle = { navController.navigate("detalle") },
                    onSolicitarArriendo = { navController.navigate("solicitudes") },
                    onRevisarSolicitudes = { navController.navigate("revisar") },
                    onVerMovimientos = { navController.navigate("movimientos") },
                    onVerInforme = { navController.navigate("informe") },
                    onReportarProblema = { navController.navigate("reporte") },
                    onVerPosts = { navController.navigate("posts") },
                    onCerrarSesion = {
                        navController.navigate("login") {
                            popUpTo("inicio") { inclusive = true }
                        }
                        authViewModel.cerrarSesion()
                    }
                )
            }
        }
        composable("solicitudes") {
            usuarioLogeado?.let { usuario ->
                MisReservasScreen(
                    emailUsuario = usuario.email,
                    onVolver = { navController.popBackStack() }
                )
            }
        }
        composable("revisar") {
            RevisarSolicitudesScreen(onVolver = { navController.popBackStack() })
        }
        composable("movimientos") {
            MovimientosScreen(onVolver = { navController.popBackStack() })
        }
        composable("informe") {
            usuarioLogeado?.let { usuario ->
                InformeScreen(
                    rol = usuario.rol,
                    onVolver = { navController.popBackStack() }
                )
            }
        }
        composable("detalle") {
            DetalleArriendoScreen(onVolver = { navController.popBackStack() })
        }
        composable("registro") {
            RegistroScreen(
                viewModel = usuarioViewModel,
                onFormularioValido = { navController.navigate("resumen") },
                onVolver = { navController.popBackStack() }
            )
        }
        composable("resumen") {
            ResumenScreen(
                viewModel = usuarioViewModel,
                onVolverInicio = { navController.popBackStack("inicio", false) }
            )
        }
        composable("reporte") {
            ReporteScreen(onVolver = { navController.popBackStack() })
        }
        composable("posts") {
            PostScreen(onVolver = { navController.popBackStack() })
        }
    }
}
