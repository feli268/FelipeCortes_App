package com.example.felipecortes_app.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.felipecortes_app.viewmodel.UsuarioViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    viewModel: UsuarioViewModel,
    onFormularioValido: () -> Unit,
    onVolver: () -> Unit
) {
    val estado by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val calendario = remember { Calendar.getInstance() }
    val selectorFecha = remember {
        DatePickerDialog(
            context,
            { _, anio, mes, dia ->
                val fechaFormateada = String.format("%02d/%02d/%04d", dia, mes + 1, anio)
                viewModel.onFechaArriendoChange(fechaFormateada)
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Solicitud de arriendo") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
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

            OutlinedTextField(
                value = estado.numeroPersonas,
                onValueChange = { viewModel.onNumeroPersonasChange(it) },
                label = { Text("Número de personas") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = estado.errores.numeroPersonas != null,
                supportingText = { estado.errores.numeroPersonas?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.fechaArriendo,
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha del arriendo") },
                isError = estado.errores.fechaArriendo != null,
                supportingText = { estado.errores.fechaArriendo?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = { selectorFecha.show() }) {
                Text("Elegir fecha")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿Requiere equipo de sonido?")
                Switch(
                    checked = estado.requiereSonido,
                    onCheckedChange = { viewModel.onRequiereSonidoChange(it) }
                )
            }

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