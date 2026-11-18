package com.example.felipecortes_app.repository

import com.example.felipecortes_app.data.local.MovimientoDao
import com.example.felipecortes_app.data.local.MovimientoEntity
import com.example.felipecortes_app.data.local.TotalCategoria
import com.example.felipecortes_app.data.model.TipoMovimiento
import kotlinx.coroutines.flow.Flow

class MovimientoRepository(private val dao: MovimientoDao) {

    val movimientos: Flow<List<MovimientoEntity>> = dao.obtenerTodos()
    val totalIngresos: Flow<Int> = dao.totalPorTipo(TipoMovimiento.INGRESO.name)
    val totalGastos: Flow<Int> = dao.totalPorTipo(TipoMovimiento.GASTO.name)

    fun totalPorTipoYPeriodo(tipo: TipoMovimiento, periodo: String): Flow<Int> =
        dao.totalPorTipoYPeriodo(tipo.name, periodo)

    fun gastosPorCategoria(periodo: String): Flow<List<TotalCategoria>> =
        dao.gastosPorCategoria(periodo)

    // Devuelve null si todo salió bien, o un texto con el error
    suspend fun registrar(
        tipo: TipoMovimiento,
        categoria: String,
        descripcion: String,
        montoTexto: String,
        fecha: String
    ): String? {
        if (descripcion.isBlank() || fecha.isBlank()) {
            return "Completa la descripción y la fecha"
        }
        val monto = montoTexto.toIntOrNull()
        if (monto == null || monto <= 0) {
            return "El monto debe ser un número mayor a 0"
        }
        dao.insertar(
            MovimientoEntity(
                tipo = tipo.name,
                categoria = categoria,
                descripcion = descripcion,
                monto = monto,
                fecha = fecha
            )
        )
        return null
    }

    suspend fun anular(id: Int) {
        dao.anular(id)
    }
}
