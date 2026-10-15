package com.example.felipecortes_app.model

// Datos del formulario de Solicitud de arriendo que llena un vecino.
data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val direccion: String = "",
    val aceptaTerminos: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores()
)

// Mensaje de error por cada campo. null significa "sin error".
data class UsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val direccion: String? = null,
    val aceptaTerminos: String? = null
)