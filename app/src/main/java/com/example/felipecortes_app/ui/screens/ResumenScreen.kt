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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.felipecortes_app.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenScreen(
    viewModel: UsuarioViewModel,
    onVolverInicio: () -> Unit
) {
    val estado by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Resumen de la solicitud") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Nombre: ${estado.nombre}")
            Text("Correo: ${estado.correo}")
            Text("Dirección: ${estado.direccion}")
            Text("Acepta términos: ${if (estado.aceptaTerminos) "Sí" else "No"}")

            Button(onClick = onVolverInicio) {
                Text("Volver al inicio")
            }
        }
    }
}