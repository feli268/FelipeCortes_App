package com.example.felipecortes_app.ui.utils

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

// Función propia que envuelve calculateWindowSizeClass() de Compose.
// Recibe el Activity (MainActivity) y devuelve solo la categoría de ANCHO
// (Compact, Medium o Expanded), que es la que usamos para decidir qué pantalla mostrar.
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun obtenerAnchoDisponible(activity: ComponentActivity): WindowWidthSizeClass {
    val windowSizeClass = calculateWindowSizeClass(activity)
    return windowSizeClass.widthSizeClass
}