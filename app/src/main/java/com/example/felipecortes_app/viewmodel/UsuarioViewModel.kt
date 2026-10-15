package com.example.felipecortes_app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.felipecortes_app.model.UsuarioErrores
import com.example.felipecortes_app.model.UsuarioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UsuarioViewModel : ViewModel() {

    // _uiState es privado y mutable (solo el ViewModel lo puede cambiar).
    private val _uiState = MutableStateFlow(UsuarioUiState())

    // uiState es la versión pública, de solo lectura, que las pantallas van a observar.
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

    fun onAceptaTerminosChange(valor: Boolean) {
        _uiState.value = _uiState.value.copy(aceptaTerminos = valor)
    }

    // Revisa cada campo, guarda los mensajes de error en el estado,
    // y devuelve true solo si no hay ningún error.
    fun validarFormulario(): Boolean {
        val estado = _uiState.value

        val errores = UsuarioErrores(
            nombre = if (estado.nombre.isBlank()) "El nombre es obligatorio" else null,
            correo = if (!estado.correo.contains("@")) "Correo inválido" else null,
            direccion = if (estado.direccion.isBlank()) "La dirección es obligatoria" else null,
            aceptaTerminos = if (!estado.aceptaTerminos) "Debes aceptar los términos" else null
        )

        _uiState.value = estado.copy(errores = errores)

        return errores.nombre == null &&
                errores.correo == null &&
                errores.direccion == null &&
                errores.aceptaTerminos == null
    }
}