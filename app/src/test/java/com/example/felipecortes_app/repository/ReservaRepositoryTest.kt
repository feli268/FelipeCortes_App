package com.example.felipecortes_app.repository

import com.example.felipecortes_app.data.local.ReservaDao
import com.example.felipecortes_app.data.local.ReservaEntity
import com.example.felipecortes_app.data.model.EstadoReserva
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

// DAO de mentira: guarda todo en una lista en vez de usar la base de datos
private class FakeReservaDao : ReservaDao {
    val reservas = mutableListOf<ReservaEntity>()
    var superpuestas = 0
    var ultimoEstado: String? = null
    var ultimaObservacion: String? = null

    override suspend fun insertar(reserva: ReservaEntity): Long {
        reservas.add(reserva)
        return reservas.size.toLong()
    }

    override fun obtenerTodas(): Flow<List<ReservaEntity>> = flowOf(reservas)

    override fun obtenerPorUsuario(email: String): Flow<List<ReservaEntity>> =
        flowOf(reservas.filter { it.usuarioEmail == email })

    override fun obtenerPorEstado(estado: String): Flow<List<ReservaEntity>> =
        flowOf(reservas.filter { it.estado == estado })

    override suspend fun actualizarEstado(id: Int, estado: String, observacion: String) {
        ultimoEstado = estado
        ultimaObservacion = observacion
    }

    override suspend fun contarSuperpuestas(
        dependenciaId: Int,
        fecha: String,
        horaInicio: String,
        horaFin: String
    ): Int = superpuestas
}

class ReservaRepositoryTest : FunSpec({

    test("una solicitud con campos vacíos devuelve error y no se guarda") {
        val dao = FakeReservaDao()
        val repository = ReservaRepository(dao)

        val error = repository.solicitar(1, "vecino@jdvzaror.cl", "", "10:00", "12:00")

        error shouldBe "Completa todos los campos"
        dao.reservas.size shouldBe 0
    }

    test("hora de fin anterior a la de inicio devuelve error") {
        val dao = FakeReservaDao()
        val repository = ReservaRepository(dao)

        val error = repository.solicitar(1, "vecino@jdvzaror.cl", "2026-12-05", "14:00", "10:00")

        error shouldBe "La hora de término debe ser posterior a la de inicio"
        dao.reservas.size shouldBe 0
    }

    test("una solicitud válida se guarda como PENDIENTE") {
        val dao = FakeReservaDao()
        val repository = ReservaRepository(dao)

        val error = repository.solicitar(1, "vecino@jdvzaror.cl", "2026-12-05", "10:00", "14:00")

        error.shouldBeNull()
        dao.reservas.size shouldBe 1
        dao.reservas.first().estado shouldBe EstadoReserva.PENDIENTE.name
    }

    test("aprobar sin choque de horario cambia el estado a APROBADA") {
        val dao = FakeReservaDao()
        val repository = ReservaRepository(dao)
        dao.superpuestas = 0
        val reserva = ReservaEntity(
            id = 1, dependenciaId = 1, usuarioEmail = "vecino@jdvzaror.cl",
            fecha = "2026-12-05", horaInicio = "10:00", horaFin = "14:00"
        )

        val error = repository.aprobar(reserva)

        error.shouldBeNull()
        dao.ultimoEstado shouldBe EstadoReserva.APROBADA.name
    }

    test("aprobar con choque devuelve error y no cambia el estado") {
        val dao = FakeReservaDao()
        val repository = ReservaRepository(dao)
        dao.superpuestas = 1
        val reserva = ReservaEntity(
            id = 2, dependenciaId = 1, usuarioEmail = "vecino@jdvzaror.cl",
            fecha = "2026-12-05", horaInicio = "12:00", horaFin = "16:00"
        )

        val error = repository.aprobar(reserva)

        error shouldBe "Ya existe una reserva aprobada en ese horario"
        dao.ultimoEstado.shouldBeNull()
    }

    test("rechazar sin motivo usa el texto por defecto") {
        val dao = FakeReservaDao()
        val repository = ReservaRepository(dao)

        repository.rechazar(1, "")

        dao.ultimoEstado shouldBe EstadoReserva.RECHAZADA.name
        dao.ultimaObservacion shouldBe "Rechazada por la directiva"
    }
})
