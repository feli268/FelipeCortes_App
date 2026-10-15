package com.example.felipecortes_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.felipecortes_app.R
import com.example.felipecortes_app.viewmodel.InicioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    viewModel: InicioViewModel = viewModel(),
    anchoDisponible: WindowWidthSizeClass,
    onVerDetalle: () -> Unit
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
        }
    }
}