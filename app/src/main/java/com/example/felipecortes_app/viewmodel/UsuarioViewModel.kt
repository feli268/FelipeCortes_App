package com.example.felipecortes_app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.felipecortes_app.model.UsuarioErrores
import com.example.felipecortes_app.model.UsuarioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UsuarioViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState

    fun onNombreChange(valor: String) {
        _uiState.value = _uiState.value.copy(nombre = valor)
    }

    fun onCorreoChange(valor: String) {
        _uiState.value = _uiState.value.copy(correo = valor)
    }

    fun onDireccionChange(valor: String) {
        _uiState.value = _uiState.value.copy(direccion = valor)
    }

    fun onNumeroPersonasChange(valor: String) {
        // Solo aceptamos dígitos, para que este campo siempre represente un número válido.
        if (valor.all { it.isDigit() }) {
            _uiState.value = _uiState.value.copy(numeroPersonas = valor)
        }
    }

    fun onFechaArriendoChange(valor: String) {
        _uiState.value = _uiState.value.copy(fechaArriendo = valor)
    }

    fun onRequiereSonidoChange(valor: Boolean) {
        _uiState.value = _uiState.value.copy(requiereSonido = valor)
    }

    fun onAceptaTerminosChange(valor: Boolean) {
        _uiState.value = _uiState.value.copy(aceptaTerminos = valor)
    }

    fun validarFormulario(): Boolean {
        val estado = _uiState.value

        val errores = UsuarioErrores(
            nombre = if (estado.nombre.isBlank()) "El nombre es obligatorio" else null,
            correo = if (!estado.correo.contains("@")) "Correo inválido" else null,
            direccion = if (estado.direccion.isBlank()) "La dirección es obligatoria" else null,
            numeroPersonas = when {
                estado.numeroPersonas.toIntOrNull() == null -> "Ingresa un número válido"
                estado.numeroPersonas.toInt() <= 0 -> "Debe ser al menos 1 persona"
                estado.numeroPersonas.toInt() > 50 -> "No puede superar las 50 personas"
                else -> null
            },
            fechaArriendo = if (estado.fechaArriendo.isBlank()) "Selecciona una fecha" else null,
            aceptaTerminos = if (!estado.aceptaTerminos) "Debes aceptar los términos" else null
        )

        _uiState.value = estado.copy(errores = errores)

        return errores.nombre == null &&
                errores.correo == null &&
                errores.direccion == null &&
                errores.numeroPersonas == null &&
                errores.fechaArriendo == null &&
                errores.aceptaTerminos == null
    }
}