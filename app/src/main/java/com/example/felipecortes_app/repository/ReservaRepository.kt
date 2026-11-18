package com.example.felipecortes_app.repository

import com.example.felipecortes_app.data.local.ReservaDao
import com.example.felipecortes_app.data.local.ReservaEntity
import com.example.felipecortes_app.data.model.EstadoReserva
import kotlinx.coroutines.flow.Flow

class ReservaRepository(private val dao: ReservaDao) {

    val todas: Flow<List<ReservaEntity>> = dao.obtenerTodas()

    val pendientes: Flow<List<ReservaEntity>> =
        dao.obtenerPorEstado(EstadoReserva.PENDIENTE.name)

    fun deUsuario(email: String): Flow<List<ReservaEntity>> =
        dao.obtenerPorUsuario(email)

    // Devuelve null si todo salió bien, o un texto con el error
    suspend fun solicitar(
        dependenciaId: Int,
        usuarioEmail: String,
        fecha: String,
        horaInicio: String,
        horaFin: String
    ): String? {
        if (fecha.isBlank() || horaInicio.isBlank() || horaFin.isBlank()) {
            return "Completa todos los campos"
        }
        if (horaFin <= horaInicio) {
            return "La hora de término debe ser posterior a la de inicio"
        }
        dao.insertar(
            ReservaEntity(
                dependenciaId = dependenciaId,
                usuarioEmail = usuarioEmail,
                fecha = fecha,
                horaInicio = horaInicio,
                horaFin = horaFin
            )
        )
        return null
    }

    // Antes de aprobar, revisa que no choque con otra reserva ya aprobada
    suspend fun aprobar(reserva: ReservaEntity): String? {
        val choques = dao.contarSuperpuestas(
            reserva.dependenciaId, reserva.fecha, reserva.horaInicio, reserva.horaFin
        )
        if (choques > 0) {
            return "Ya existe una reserva aprobada en ese horario"
        }
        dao.actualizarEstado(reserva.id, EstadoReserva.APROBADA.name, "Aprobada por la directiva")
        return null
    }

    suspend fun rechazar(id: Int, motivo: String) {
        dao.actualizarEstado(
            id,
            EstadoReserva.RECHAZADA.name,
            motivo.ifBlank { "Rechazada por la directiva" }
        )
    }

    suspend fun cancelar(id: Int) {
        dao.actualizarEstado(id, EstadoReserva.CANCELADA.name, "Cancelada por el solicitante")
    }
}
