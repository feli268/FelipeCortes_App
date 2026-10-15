package com.example.felipecortes_app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Pantalla de detalle de un arriendo. Datos fijos por ahora (se conectarán a datos reales en una guía futura).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleArriendoScreen(onVolver: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Detalle del arriendo") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Espacio: Sede Social")
            Text("Fecha: 20 de octubre")
            Text("Estado: Pendiente de pago")

            Button(onClick = onVolver) {
                Text("Volver")
            }
        }
    }
}