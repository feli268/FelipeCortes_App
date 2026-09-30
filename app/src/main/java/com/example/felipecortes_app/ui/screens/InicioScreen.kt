package com.example.felipecortes_app.ui.screens

import androidx.compose.runtime.Composable
import com.example.felipecortes_app.viewmodel.InicioViewModel
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import com.example.felipecortes_app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(viewModel: InicioViewModel = viewModel())
{
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(viewModel.nombreOrganizacion) })
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de la Junta de Vecinos",
                modifier = Modifier.size(96.dp)
            )
            Text("Bienvenido")
            Text("Arriendos este mes: ${viewModel.arriendosDelMes}")
        }
    }
}