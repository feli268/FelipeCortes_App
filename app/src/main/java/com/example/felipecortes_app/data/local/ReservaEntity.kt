package com.example.felipecortes_app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.felipecortes_app.data.model.EstadoReserva

@Entity(tableName = "reserva_local")
data class ReservaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dependenciaId: Int,
    val usuarioEmail: String,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val estado: String = EstadoReserva.PENDIENTE.name,
    val observacion: String = "",
    val pendienteSync: Boolean = true
)
