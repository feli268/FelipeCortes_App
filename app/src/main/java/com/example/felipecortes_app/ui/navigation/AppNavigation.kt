package com.example.felipecortes_app.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.felipecortes_app.ui.screens.DetalleArriendoScreen
import com.example.felipecortes_app.ui.screens.InicioScreen

@Composable
fun AppNavigation(anchoDisponible: WindowWidthSizeClass) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "inicio") {
        composable("inicio") {
            InicioScreen(
                anchoDisponible = anchoDisponible,
                onVerDetalle = { navController.navigate("detalle") }
            )
        }
        composable("detalle") {
            DetalleArriendoScreen(onVolver = { navController.popBackStack() })
        }
    }
}