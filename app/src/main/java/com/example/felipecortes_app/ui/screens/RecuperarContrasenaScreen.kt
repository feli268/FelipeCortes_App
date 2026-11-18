package com.example.felipecortes_app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.felipecortes_app.viewmodel.AuthViewModel

@Composable
fun RecuperarContrasenaScreen(
    authViewModel: AuthViewModel,
    onVolver: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Recuperar contraseña",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ingresa tu correo y te enviaremos las instrucciones para restablecerla.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isBlank()) {
                    mensaje = "Escribe tu correo"
                } else {
                    // En un sistema real aquí se enviaría el correo.
                    // En este MVP solo se consulta; el mensaje es el mismo exista o no el correo.
                    authViewModel.recuperarContrasena(email.trim())
                    mensaje = "Si el correo está registrado, te enviamos las instrucciones."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar instrucciones")
        }

        mensaje?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = it, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onVolver) {
            Text("Volver al inicio de sesión")
        }
    }
}
