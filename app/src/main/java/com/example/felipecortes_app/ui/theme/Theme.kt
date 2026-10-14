package com.example.felipecortes_app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Naranja80,
    secondary = Cafe80,
    tertiary = Verde80,
    background = FondoOscuro,
    surface = FondoOscuro,
    onBackground = TextoOscuro,
    onSurface = TextoOscuro
)

private val LightColorScheme = lightColorScheme(
    primary = Naranja40,
    secondary = Cafe40,
    tertiary = Verde40,
    background = FondoClaro,
    surface = FondoClaro,
    onBackground = TextoClaro,
    onSurface = TextoClaro
)

@Composable
fun FelipeCortes_AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Lo dejamos en false para que siempre se use la paleta propia de la app,
    // en vez de la paleta automática de Android 12+ basada en el fondo de pantalla.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )



}