package com.example.felipecortes_app.data.model

data class Dependencia(
    val id: Int,
    val nombre: String
)

object Dependencias {
    val lista = listOf(
        Dependencia(1, "Sede social"),
        Dependencia(2, "Sala multiuso"),
        Dependencia(3, "Cancha comunitaria")
    )

    fun nombrePorId(id: Int): String =
        lista.find { it.id == id }?.nombre ?: "Dependencia $id"
}
