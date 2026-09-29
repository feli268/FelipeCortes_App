package com.example.felipecortes_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.felipecortes_app.ui.screens.InicioScreen
import com.example.felipecortes_app.ui.theme.FelipeCortes_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FelipeCortes_AppTheme {
                InicioScreen()
            }
        }
    }
}