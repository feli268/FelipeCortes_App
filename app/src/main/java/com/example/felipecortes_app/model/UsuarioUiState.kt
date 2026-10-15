package com.example.felipecortes_app.model

// Datos del formulario de Solicitud de arriendo que llena un vecino.
data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val direccion: String = "",
    val numeroPersonas: String = "",   // campo numérico (Int), guardado como texto y validado
    val fechaArriendo: String = "",    // campo de fecha (Date), formato dd/MM/yyyy
    val requiereSonido: Boolean = false, // switch
    val aceptaTerminos: Boolean = false, // checkbox
    val errores: UsuarioErrores = UsuarioErrores()
)

data class UsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val direccion: String? = null,
    val numeroPersonas: String? = null,
    val fechaArriendo: String? = null,
    val aceptaTerminos: String? = null
)