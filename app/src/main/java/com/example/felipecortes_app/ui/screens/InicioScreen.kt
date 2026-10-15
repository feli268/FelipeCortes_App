package com.example.felipecortes_app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.felipecortes_app.R
import com.example.felipecortes_app.repository.AjustesRepository
import com.example.felipecortes_app.viewmodel.InicioViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    viewModel: InicioViewModel = viewModel(),
    anchoDisponible: WindowWidthSizeClass,
    onVerDetalle: () -> Unit,
    onSolicitarArriendo: () -> Unit,
    onReportarProblema: () -> Unit
) {
    val tamanoLogo = when (anchoDisponible) {
        WindowWidthSizeClass.Compact -> 96.dp
        WindowWidthSizeClass.Medium -> 140.dp
        else -> 200.dp
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(viewModel.nombreOrganizacion) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de la Junta de Vecinos",
                modifier = Modifier.size(tamanoLogo)
            )

            Text(
                text = "Bienvenido/a",
                color = MaterialTheme.colorScheme.primary
            )

            Text("Arriendos este mes: ${viewModel.arriendosDelMes}")

            Button(onClick = onVerDetalle) {
                Text("Ver detalle de arriendo")
            }

            Button(onClick = onSolicitarArriendo) {
                Text("Solicitar arriendo")
            }

            Button(onClick = onReportarProblema) {
                Text("Reportar un problema")
            }

            SeccionModoTesorero()
        }
    }
}

@Composable
private fun SeccionModoTesorero() {
    val context = LocalContext.current
    val alcance = rememberCoroutineScope()

    val modoGuardado by AjustesRepository.leerModoEspecial(context).collectAsState(initial = null)
    var mostrarDetalles by rememberSaveable { mutableStateOf(false) }

    val etiquetaModo by remember(modoGuardado) {
        derivedStateOf {
            when (modoGuardado) {
                true -> "Modo tesorero: activado"
                false -> "Modo tesorero: desactivado"
                null -> "Cargando..."
            }
        }
    }

    val colorPanel by animateColorAsState(
        targetValue = if (modoGuardado == true) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surface,
        label = "colorModoTesorero"
    )

    if (modoGuardado == null) {
        CircularProgressIndicator()
    } else {
        Column(
            modifier = Modifier
                .background(colorPanel, shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(etiquetaModo)
                Switch(
                    checked = modoGuardado == true,
                    onCheckedChange = { nuevoValor ->
                        alcance.launch {
                            AjustesRepository.guardarModoEspecial(context, nuevoValor)
                        }
                    }
                )
            }

            Button(onClick = { mostrarDetalles = !mostrarDetalles }) {
                Text(if (mostrarDetalles) "Ocultar detalles" else "Ver detalles")
            }

            AnimatedVisibility(visible = mostrarDetalles) {
                Text("El modo tesorero mostrará información financiera adicional en una futura guía.")
            }
        }
    }
}