package com.example.felipecortes_app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservaDao {

    @Insert
    suspend fun insertar(reserva: ReservaEntity): Long

    @Query("SELECT * FROM reserva_local ORDER BY fecha, horaInicio")
    fun obtenerTodas(): Flow<List<ReservaEntity>>

    @Query("SELECT * FROM reserva_local WHERE usuarioEmail = :email ORDER BY fecha, horaInicio")
    fun obtenerPorUsuario(email: String): Flow<List<ReservaEntity>>

    @Query("SELECT * FROM reserva_local WHERE estado = :estado ORDER BY fecha, horaInicio")
    fun obtenerPorEstado(estado: String): Flow<List<ReservaEntity>>

    @Query("UPDATE reserva_local SET estado = :estado, observacion = :observacion WHERE id = :id")
    suspend fun actualizarEstado(id: Int, estado: String, observacion: String)

    @Query(
        "SELECT COUNT(*) FROM reserva_local " +
        "WHERE dependenciaId = :dependenciaId AND fecha = :fecha AND estado = 'APROBADA' " +
        "AND horaInicio < :horaFin AND horaFin > :horaInicio"
    )
    suspend fun contarSuperpuestas(dependenciaId: Int, fecha: String, horaInicio: String, horaFin: String): Int
}
