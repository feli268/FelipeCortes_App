package com.example.felipecortes_app.data.model

enum class TipoMovimiento {
    INGRESO,
    GASTO
}

object CategoriasMovimiento {
    val lista = listOf(
        "Arriendo",
        "Consumo eléctrico",
        "Agua",
        "Gas",
        "Mantención",
        "Otros"
    )
}
