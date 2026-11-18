package com.example.felipecortes_app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.felipecortes_app.data.local.ReservaEntity
import com.example.felipecortes_app.data.model.Dependencias
import com.example.felipecortes_app.data.model.EstadoReserva
import com.example.felipecortes_app.viewmodel.ReservaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisReservasScreen(
    emailUsuario: String,
    onVolver: () -> Unit,
    viewModel: ReservaViewModel = viewModel()
) {
    var dependenciaId by remember { mutableStateOf(Dependencias.lista.first().id) }
    var fecha by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var horaFin by remember { mutableStateOf("") }

    val mensaje by viewModel.mensaje.collectAsState()
    val reservasFlow = remember(emailUsuario) { viewModel.reservasDe(emailUsuario) }
    val reservas by reservasFlow.collectAsState(initial = emptyList())
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.limpiarMensaje()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Mis reservas") },
                navigationIcon = { TextButton(onClick = onVolver) { Text("Volver") } }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Nueva solicitud",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Dependencias.lista.forEach { dependencia ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = dependenciaId == dependencia.id,
                        onClick = { dependenciaId = dependencia.id }
                    )
                    Text(dependencia.nombre)
                }
            }

            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha (AAAA-MM-DD)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = horaInicio,
                    onValueChange = { horaInicio = it },
                    label = { Text("Inicio (HH:MM)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = horaFin,
                    onValueChange = { horaFin = it },
                    label = { Text("Fin (HH:MM)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Button(
                onClick = {
                    viewModel.solicitar(
                        dependenciaId,
                        emailUsuario,
                        fecha.trim(),
                        horaInicio.trim(),
                        horaFin.trim()
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar solicitud")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Mis solicitudes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (reservas.isEmpty()) {
                Text("Aún no tienes solicitudes")
            } else {
                reservas.forEach { reserva ->
                    TarjetaReserva(
                        reserva = reserva,
                        onCancelar = { viewModel.cancelar(reserva.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaReserva(reserva: ReservaEntity, onCancelar: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                Dependencias.nombrePorId(reserva.dependenciaId),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text("${reserva.fecha} · ${reserva.horaInicio} a ${reserva.horaFin}")
            Text("Estado: ${reserva.estado}")
            if (reserva.observacion.isNotBlank()) {
                Text(
                    reserva.observacion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (reserva.estado == EstadoReserva.PENDIENTE.name) {
                OutlinedButton(onClick = onCancelar) {
                    Text("Cancelar solicitud")
                }
            }
        }
    }
}
