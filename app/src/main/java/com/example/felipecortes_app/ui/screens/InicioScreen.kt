package com.example.felipecortes_app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
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
    onReportarProblema: () -> Unit,
) {
    val tamanoLogo = when (anchoDisponible) {
        WindowWidthSizeClass.Compact -> 56.dp
        WindowWidthSizeClass.Medium -> 80.dp
        else -> 110.dp
    }

    Scaffold { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            EncabezadoInicio(nombreOrganizacion = viewModel.nombreOrganizacion)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TarjetaResumen(arriendosDelMes = viewModel.arriendosDelMes, tamanoLogo = tamanoLogo)
                TarjetaAccion(
                    titulo = "Ver detalle de arriendo",
                    subtitulo = "Revisa el estado del arriendo actual",
                    onClick = onVerDetalle
                )
                TarjetaAccion(
                    titulo = "Solicitar arriendo",
                    subtitulo = "Pide un espacio comunitario",
                    onClick = onSolicitarArriendo
                )
                TarjetaAccion(
                    titulo = "Reportar un problema",
                    subtitulo = "Avisa a la directiva sobre un espacio",
                    onClick = onReportarProblema
                )
                SeccionModoTesorero()
            }
        }
    }
}

@Composable
private fun EncabezadoInicio(nombreOrganizacion: String) {
    Surface(color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(56.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Text("JV", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
            Column {
                Text("¡Hola, vecino/a!", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                Text(nombreOrganizacion, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
private fun TarjetaResumen(arriendosDelMes: Int, tamanoLogo: Dp) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de la Junta de Vecinos",
                modifier = Modifier.size(tamanoLogo)
            )
            Column {
                Text("Arriendos este mes", style = MaterialTheme.typography.labelMedium)
                Text(
                    "$arriendosDelMes",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TarjetaAccion(titulo: String, subtitulo: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                subtitulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
        targetValue = if (modoGuardado == true) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        label = "colorModoTesorero"
    )
    if (modoGuardado == null) {
        CircularProgressIndicator()
    } else {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = colorPanel)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(etiquetaModo)
                    Switch(
                        checked = modoGuardado == true,
                        onCheckedChange = { nuevoValor ->
                            alcance.launch { AjustesRepository.guardarModoEspecial(context, nuevoValor) }
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
}