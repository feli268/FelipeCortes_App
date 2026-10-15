package com.example.felipecortes_app.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.felipecortes_app.ui.screens.DetalleArriendoScreen
import com.example.felipecortes_app.ui.screens.InicioScreen
import com.example.felipecortes_app.ui.screens.RegistroScreen
import com.example.felipecortes_app.ui.screens.ResumenScreen
import com.example.felipecortes_app.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation(anchoDisponible: WindowWidthSizeClass) {
    val navController = rememberNavController()

    // Una sola instancia de UsuarioViewModel, compartida entre "registro" y "resumen"
    // para que ambas pantallas lean los mismos datos del formulario.
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(navController = navController, startDestination = "inicio") {
        composable("inicio") {
            InicioScreen(
                anchoDisponible = anchoDisponible,
                onVerDetalle = { navController.navigate("detalle") },
                onSolicitarArriendo = { navController.navigate("registro") }
            )
        }
        composable("detalle") {
            DetalleArriendoScreen(onVolver = { navController.popBackStack() })
        }
        composable("registro") {
            RegistroScreen(
                viewModel = usuarioViewModel,
                onFormularioValido = { navController.navigate("resumen") }
            )
        }
        composable("resumen") {
            ResumenScreen(
                viewModel = usuarioViewModel,
                onVolverInicio = { navController.popBackStack("inicio", false) }
            )
        }
    }
}