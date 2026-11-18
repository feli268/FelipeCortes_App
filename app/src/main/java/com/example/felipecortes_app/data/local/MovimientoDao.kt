package com.example.felipecortes_app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimientoDao {

    @Insert
    suspend fun insertar(movimiento: MovimientoEntity): Long

    @Query("SELECT * FROM movimiento_local ORDER BY fecha DESC, id DESC")
    fun obtenerTodos(): Flow<List<MovimientoEntity>>

    @Query("UPDATE movimiento_local SET anulado = 1 WHERE id = :id")
    suspend fun anular(id: Int)

    @Query("SELECT COALESCE(SUM(monto), 0) FROM movimiento_local WHERE tipo = :tipo AND anulado = 0")
    fun totalPorTipo(tipo: String): Flow<Int>

    @Query(
        "SELECT COALESCE(SUM(monto), 0) FROM movimiento_local " +
        "WHERE tipo = :tipo AND anulado = 0 AND fecha LIKE :periodo || '%'"
    )
    fun totalPorTipoYPeriodo(tipo: String, periodo: String): Flow<Int>

    @Query(
        "SELECT categoria, SUM(monto) AS total FROM movimiento_local " +
        "WHERE tipo = 'GASTO' AND anulado = 0 AND fecha LIKE :periodo || '%' " +
        "GROUP BY categoria ORDER BY total DESC"
    )
    fun gastosPorCategoria(periodo: String): Flow<List<TotalCategoria>>
}
