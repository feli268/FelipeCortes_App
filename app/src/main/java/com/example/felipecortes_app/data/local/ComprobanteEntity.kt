package com.example.felipecortes_app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comprobante_local")
data class ComprobanteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val movimientoId: Int,
    val nombreArchivo: String,
    val uriLocal: String,
    val fechaAdjunto: String,
    val pendienteSync: Boolean = true
)
