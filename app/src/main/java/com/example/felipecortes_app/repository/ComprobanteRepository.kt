package com.example.felipecortes_app.repository

import com.example.felipecortes_app.data.local.ComprobanteDao
import com.example.felipecortes_app.data.local.ComprobanteEntity
import kotlinx.coroutines.flow.Flow

class ComprobanteRepository(private val dao: ComprobanteDao) {

    val comprobantes: Flow<List<ComprobanteEntity>> = dao.obtenerTodos()

    // Devuelve null si todo salió bien, o un texto con el error
    suspend fun adjuntar(
        movimientoId: Int,
        nombreArchivo: String,
        uriLocal: String,
        fecha: String
    ): String? {
        if (nombreArchivo.isBlank() || uriLocal.isBlank()) {
            return "No se pudo guardar la imagen"
        }
        dao.insertar(
            ComprobanteEntity(
                movimientoId = movimientoId,
                nombreArchivo = nombreArchivo,
                uriLocal = uriLocal,
                fechaAdjunto = fecha
            )
        )
        return null
    }
}
