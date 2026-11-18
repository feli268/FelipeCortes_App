package com.example.felipecortes_app.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.felipecortes_app.data.local.ComprobanteEntity
import com.example.felipecortes_app.data.local.MovimientoEntity
import com.example.felipecortes_app.data.model.CategoriasMovimiento
import com.example.felipecortes_app.data.model.TipoMovimiento
import com.example.felipecortes_app.viewmodel.ComprobanteViewModel
import com.example.felipecortes_app.viewmodel.MovimientoViewModel
import kotlinx.coroutines.launch
import java.io.File

private fun formatoPesos(monto: Int): String =
    "\$" + "%,d".format(monto).replace(',', '.')

// Crea un archivo nuevo dentro de la carpeta privada "comprobantes" de la app
private fun crearArchivoComprobante(context: Context): File {
    val carpeta = File(context.filesDir, "comprobantes")
    carpeta.mkdirs()
    return File(carpeta, "comprobante_${System.currentTimeMillis()}.jpg")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovimientosScreen(
    onVolver: () -> Unit,
    viewModel: MovimientoViewModel = viewModel(),
    comprobanteViewModel: ComprobanteViewModel = viewModel()
) {
    val context = LocalContext.current
    val alcance = rememberCoroutineScope()

    var tipo by remember { mutableStateOf(TipoMovimiento.INGRESO) }
    var categoria by remember { mutableStateOf(CategoriasMovimiento.lista.first()) }
    var descripcion by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    // Estado del flujo de adjuntar comprobante
    var mostrarOpciones by rememberSaveable { mutableStateOf(false) }
    var movimientoParaAdjuntar by rememberSaveable { mutableStateOf<Int?>(null) }
    var rutaPendiente by rememberSaveable { mutableStateOf<String?>(null) }

    val movimientos by viewModel.movimientos.collectAsState()
    val totalIngresos by viewModel.totalIngresos.collectAsState()
    val totalGastos by viewModel.totalGastos.collectAsState()
    val saldo by viewModel.saldo.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val comprobantes by comprobanteViewModel.comprobantes.collectAsState()
    val mensajeComprobante by comprobanteViewModel.mensaje.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbarHostState.showSnackbar(it)
            // Si se registró bien, deja limpios los campos para el siguiente
            if (it == "Movimiento registrado") {
                descripcion = ""
                monto = ""
            }
            viewModel.limpiarMensaje()
        }
    }

    LaunchedEffect(mensajeComprobante) {
        mensajeComprobante?.let {
            snackbarHostState.showSnackbar(it)
            comprobanteViewModel.limpiarMensaje()
        }
    }

    // Cámara: la app de cámara guarda la foto en el archivo que le indicamos
    val tomarFoto = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { exito ->
        val ruta = rutaPendiente
        val movimientoId = movimientoParaAdjuntar
        if (exito && ruta != null && movimientoId != null) {
            comprobanteViewModel.adjuntar(movimientoId, File(ruta).name, ruta)
        } else {
            ruta?.let { File(it).delete() }
        }
        rutaPendiente = null
    }

    fun lanzarCamara() {
        val archivo = crearArchivoComprobante(context)
        rutaPendiente = archivo.absolutePath
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)
        tomarFoto.launch(uri)
    }

    // Permiso de cámara: se pide la primera vez
    val pedirPermisoCamara = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        if (concedido) {
            lanzarCamara()
        } else {
            alcance.launch { snackbarHostState.showSnackbar("Permiso de cámara denegado") }
        }
    }

    // Galería: se copia la imagen elegida a la carpeta privada de la app
    val elegirDeGaleria = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        val movimientoId = movimientoParaAdjuntar
        if (uri != null && movimientoId != null) {
            val destino = crearArchivoComprobante(context)
            try {
                context.contentResolver.openInputStream(uri)?.use { entrada ->
                    destino.outputStream().use { salida -> entrada.copyTo(salida) }
                }
                comprobanteViewModel.adjuntar(movimientoId, destino.name, destino.absolutePath)
            } catch (e: Exception) {
                destino.delete()
                alcance.launch { snackbarHostState.showSnackbar("No se pudo leer la imagen") }
            }
        }
    }

    if (mostrarOpciones) {
        AlertDialog(
            onDismissRequest = { mostrarOpciones = false },
            title = { Text("Adjuntar comprobante") },
            text = { Text("Usa solo imágenes ficticias o anonimizadas.") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarOpciones = false
                    val permisoConcedido = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                    if (permisoConcedido) {
                        lanzarCamara()
                    } else {
                        pedirPermisoCamara.launch(Manifest.permission.CAMERA)
                    }
                }) { Text("Tomar foto") }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = {
                        mostrarOpciones = false
                        elegirDeGaleria.launch("image/*")
                    }) { Text("Galería") }
                    TextButton(onClick = { mostrarOpciones = false }) { Text("Cancelar") }
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Movimientos financieros") },
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
            TarjetaResumenFinanzas(totalIngresos, totalGastos, saldo)

            Text(
                "Nuevo movimiento",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TipoMovimiento.values().forEach { opcion ->
                    FilterChip(
                        selected = tipo == opcion,
                        onClick = { tipo = opcion },
                        label = { Text(opcion.name) }
                    )
                }
            }

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CategoriasMovimiento.lista.forEach { opcion ->
                    FilterChip(
                        selected = categoria == opcion,
                        onClick = { categoria = opcion },
                        label = { Text(opcion) }
                    )
                }
            }

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = monto,
                    onValueChange = { monto = it },
                    label = { Text("Monto") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text("Fecha (AAAA-MM-DD)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Button(
                onClick = { viewModel.registrar(tipo, categoria, descripcion, monto, fecha) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar movimiento")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Historial",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (movimientos.isEmpty()) {
                Text("Aún no hay movimientos registrados")
            } else {
                movimientos.forEach { movimiento ->
                    TarjetaMovimiento(
                        movimiento = movimiento,
                        comprobantes = comprobantes.filter { it.movimientoId == movimiento.id },
                        onAnular = { viewModel.anular(movimiento.id) },
                        onAdjuntar = {
                            movimientoParaAdjuntar = movimiento.id
                            mostrarOpciones = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaResumenFinanzas(ingresos: Int, gastos: Int, saldo: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Resumen", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Ingresos: ${formatoPesos(ingresos)}")
            Text("Gastos: ${formatoPesos(gastos)}")
            Text(
                "Saldo: ${formatoPesos(saldo)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun TarjetaMovimiento(
    movimiento: MovimientoEntity,
    comprobantes: List<ComprobanteEntity>,
    onAnular: () -> Unit,
    onAdjuntar: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                "${movimiento.tipo} · ${movimiento.categoria}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(movimiento.descripcion)
            Text("${formatoPesos(movimiento.monto)} · ${movimiento.fecha}")
            Text(
                "Comprobantes: ${comprobantes.size}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (comprobantes.isNotEmpty()) {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    comprobantes.forEach { comprobante ->
                        MiniaturaComprobante(comprobante.uriLocal)
                    }
                }
            }
            if (movimiento.anulado) {
                Text(
                    "ANULADO",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onAdjuntar) {
                        Text("Adjuntar comprobante")
                    }
                    OutlinedButton(onClick = onAnular) {
                        Text("Anular")
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniaturaComprobante(ruta: String) {
    // inSampleSize = 8 carga la imagen reducida para no gastar memoria
    val imagen = remember(ruta) {
        val opciones = BitmapFactory.Options().apply { inSampleSize = 8 }
        BitmapFactory.decodeFile(ruta, opciones)?.asImageBitmap()
    }
    if (imagen != null) {
        Image(
            bitmap = imagen,
            contentDescription = "Comprobante adjunto",
            modifier = Modifier.size(64.dp)
        )
    }
}
