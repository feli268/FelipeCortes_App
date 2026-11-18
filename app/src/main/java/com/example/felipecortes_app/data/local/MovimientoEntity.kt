package com.example.felipecortes_app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movimiento_local")
data class MovimientoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tipo: String,
    val categoria: String,
    val descripcion: String,
    val monto: Int,
    val fecha: String,
    val reservaId: Int? = null,
    val anulado: Boolean = false,
    val pendienteSync: Boolean = true
)
