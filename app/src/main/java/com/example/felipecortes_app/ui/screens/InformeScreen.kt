package com.example.felipecortes_app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.felipecortes_app.data.model.Rol
import com.example.felipecortes_app.viewmodel.InformeViewModel

private fun pesos(monto: Int): String =
    "\$" + "%,d".format(monto).replace(',', '.')

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformeScreen(
    rol: Rol,
    onVolver: () -> Unit,
    viewModel: InformeViewModel = viewModel()
) {
    val periodo by viewModel.periodo.collectAsState()
    val ingresos by viewModel.ingresos.collectAsState()
    val gastos by viewModel.gastos.collectAsState()
    val saldo by viewModel.saldo.collectAsState()
    val gastosPorCategoria by viewModel.gastosPorCategoria.collectAsState()

    var textoPeriodo by remember { mutableStateOf(periodo) }

    // Los vecinos ven solo el resumen; el resto de los perfiles ve también el detalle por categoría
    val veDetalle = rol != Rol.VECINO

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Informe financiero") },
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
            OutlinedTextField(
                value = textoPeriodo,
                onValueChange = {
                    textoPeriodo = it
                    viewModel.cambiarPeriodo(it)
                },
                label = { Text("Período (AAAA-MM, vacío = todo)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        "Resumen · ${if (periodo.isBlank()) "Todos los períodos" else periodo}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Ingresos: ${pesos(ingresos)}")
                    Text("Gastos: ${pesos(gastos)}")
                    Text(
                        "Saldo: ${pesos(saldo)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (veDetalle) {
                Text(
                    "Gastos por categoría",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (gastosPorCategoria.isEmpty()) {
                    Text("Sin gastos en este período")
                } else {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            gastosPorCategoria.forEach { fila ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(fila.categoria)
                                    Text(pesos(fila.total))
                                }
                            }
                        }
                    }
                }
            } else {
                Text(
                    "Información resumida para vecinos. El detalle de movimientos es de uso interno de la directiva y tesorería.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
