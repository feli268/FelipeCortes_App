package com.example.felipecortes_app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.felipecortes_app.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    viewModel: UsuarioViewModel,
    onFormularioValido: () -> Unit
) {
    // collectAsState() convierte el StateFlow del ViewModel en un valor que Compose observa:
    // cada vez que el estado cambia, esta pantalla se vuelve a dibujar sola.
    val estado by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Solicitud de arriendo") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = { viewModel.onNombreChange(it) },
                label = { Text("Nombre") },
                isError = estado.errores.nombre != null,
                supportingText = { estado.errores.nombre?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.correo,
                onValueChange = { viewModel.onCorreoChange(it) },
                label = { Text("Correo") },
                isError = estado.errores.correo != null,
                supportingText = { estado.errores.correo?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.direccion,
                onValueChange = { viewModel.onDireccionChange(it) },
                label = { Text("Dirección") },
                isError = estado.errores.direccion != null,
                supportingText = { estado.errores.direccion?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = estado.aceptaTerminos,
                    onCheckedChange = { viewModel.onAceptaTerminosChange(it) }
                )
                Text("Acepto los términos y condiciones")
            }
            estado.errores.aceptaTerminos?.let { Text(it) }

            Button(onClick = {
                if (viewModel.validarFormulario()) {
                    onFormularioValido()
                }
            }) {
                Text("Enviar solicitud")
            }
        }
    }
}