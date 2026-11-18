package com.example.felipecortes_app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ComprobanteDao {

    @Insert
    suspend fun insertar(comprobante: ComprobanteEntity): Long

    @Query("SELECT * FROM comprobante_local ORDER BY id DESC")
    fun obtenerTodos(): Flow<List<ComprobanteEntity>>

    @Query("SELECT * FROM comprobante_local WHERE movimientoId = :movimientoId ORDER BY id DESC")
    fun obtenerPorMovimiento(movimientoId: Int): Flow<List<ComprobanteEntity>>
}
