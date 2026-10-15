package com.example.felipecortes_app.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.felipecortes_app.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenScreen(
    viewModel: UsuarioViewModel,
    onVolverInicio: () -> Unit
) {
    val estado by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen de la solicitud") },
                navigationIcon = {
                    IconButton(onClick = onVolverInicio) {
                        Text("←", fontSize = 20.sp)
                    }
                }
            )
        }
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
            Text("Número de personas: ${estado.numeroPersonas}")
            Text("Fecha del arriendo: ${estado.fechaArriendo}")
            Text("Requiere equipo de sonido: ${if (estado.requiereSonido) "Sí" else "No"}")
            Text("Acepta términos: ${if (estado.aceptaTerminos) "Sí" else "No"}")

            Button(onClick = {
                val texto = """
                    Solicitud de arriendo - Junta de Vecinos Población Zaror
                    Nombre: ${estado.nombre}
                    Correo: ${estado.correo}
                    Dirección: ${estado.direccion}
                    Número de personas: ${estado.numeroPersonas}
                    Fecha del arriendo: ${estado.fechaArriendo}
                    Requiere equipo de sonido: ${if (estado.requiereSonido) "Sí" else "No"}
                """.trimIndent()

                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, texto)
                }
                context.startActivity(Intent.createChooser(intent, "Compartir solicitud"))
            }) {
                Text("Compartir resumen")
            }
        }
    }
}