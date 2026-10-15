package com.example.felipecortes_app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.felipecortes_app.model.ReporteErrores
import com.example.felipecortes_app.model.ReporteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReporteViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ReporteUiState())
    val uiState: StateFlow<ReporteUiState> = _uiState

    fun onEspacioChange(valor: String) {
        _uiState.value = _uiState.value.copy(espacio = valor, enviado = false)
    }

    fun onDescripcionChange(valor: String) {
        _uiState.value = _uiState.value.copy(descripcion = valor, enviado = false)
    }

    fun enviarReporte() {
        val estado = _uiState.value

        val errores = ReporteErrores(
            espacio = if (estado.espacio.isBlank()) "Indica el espacio" else null,
            descripcion = if (estado.descripcion.isBlank()) "Describe el problema" else null
        )

        val esValido = errores.espacio == null && errores.descripcion == null

        _uiState.value = estado.copy(errores = errores, enviado = esValido)
    }
}