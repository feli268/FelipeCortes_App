package com.example.felipecortes_app.repository

import com.example.felipecortes_app.data.local.MovimientoDao
import com.example.felipecortes_app.data.local.MovimientoEntity
import com.example.felipecortes_app.data.local.TotalCategoria
import com.example.felipecortes_app.data.model.TipoMovimiento
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

// DAO de mentira: guarda todo en una lista en vez de usar la base de datos
private class FakeMovimientoDao : MovimientoDao {
    val movimientos = mutableListOf<MovimientoEntity>()
    var idAnulado: Int? = null

    override suspend fun insertar(movimiento: MovimientoEntity): Long {
        movimientos.add(movimiento)
        return movimientos.size.toLong()
    }

    override fun obtenerTodos(): Flow<List<MovimientoEntity>> = flowOf(movimientos)

    override suspend fun anular(id: Int) {
        idAnulado = id
    }

    override fun totalPorTipo(tipo: String): Flow<Int> = flowOf(0)

    override fun totalPorTipoYPeriodo(tipo: String, periodo: String): Flow<Int> = flowOf(0)

    override fun gastosPorCategoria(periodo: String): Flow<List<TotalCategoria>> = flowOf(emptyList())
}

class MovimientoRepositoryTest : FunSpec({

    test("monto cero devuelve error y no se guarda") {
        val dao = FakeMovimientoDao()
        val repository = MovimientoRepository(dao)

        val error = repository.registrar(TipoMovimiento.GASTO, "Agua", "Cuenta del agua", "0", "2026-11-12")

        error shouldBe "El monto debe ser un número mayor a 0"
        dao.movimientos.size shouldBe 0
    }

    test("monto con texto devuelve error") {
        val dao = FakeMovimientoDao()
        val repository = MovimientoRepository(dao)

        val error = repository.registrar(TipoMovimiento.GASTO, "Agua", "Cuenta del agua", "abc", "2026-11-12")

        error shouldBe "El monto debe ser un número mayor a 0"
        dao.movimientos.size shouldBe 0
    }

    test("monto negativo devuelve error") {
        val dao = FakeMovimientoDao()
        val repository = MovimientoRepository(dao)

        val error = repository.registrar(TipoMovimiento.INGRESO, "Arriendo", "Arriendo sede", "-500", "2026-11-12")

        error shouldBe "El monto debe ser un número mayor a 0"
    }

    test("descripción vacía devuelve error") {
        val dao = FakeMovimientoDao()
        val repository = MovimientoRepository(dao)

        val error = repository.registrar(TipoMovimiento.GASTO, "Agua", "", "12000", "2026-11-12")

        error shouldBe "Completa la descripción y la fecha"
        dao.movimientos.size shouldBe 0
    }

    test("un movimiento válido se guarda con sus datos") {
        val dao = FakeMovimientoDao()
        val repository = MovimientoRepository(dao)

        val error = repository.registrar(TipoMovimiento.GASTO, "Agua", "Cuenta del agua", "12000", "2026-11-12")

        error.shouldBeNull()
        dao.movimientos.size shouldBe 1
        val guardado = dao.movimientos.first()
        guardado.tipo shouldBe "GASTO"
        guardado.categoria shouldBe "Agua"
        guardado.monto shouldBe 12000
    }

    test("anular marca el movimiento indicado") {
        val dao = FakeMovimientoDao()
        val repository = MovimientoRepository(dao)

        repository.anular(7)

        dao.idAnulado shouldBe 7
    }
})
