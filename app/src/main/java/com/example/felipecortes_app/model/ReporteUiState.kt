package com.example.felipecortes_app.model

data class ReporteUiState(
    val espacio: String = "",
    val descripcion: String = "",
    val enviado: Boolean = false,
    val errores: ReporteErrores = ReporteErrores()
)

data class ReporteErrores(
    val espacio: String? = null,
    val descripcion: String? = null
)