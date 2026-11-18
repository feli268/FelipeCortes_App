package com.example.felipecortes_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.felipecortes_app.data.local.AppDatabase
import com.example.felipecortes_app.data.local.ReservaEntity
import com.example.felipecortes_app.data.model.Dependencias
import com.example.felipecortes_app.repository.ReservaRepository
import com.example.felipecortes_app.ui.utils.NotificacionesHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReservaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ReservaRepository(
        AppDatabase.obtenerInstancia(application).reservaDao()
    )

    val pendientes: StateFlow<List<ReservaEntity>> = repository.pendientes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todas: StateFlow<List<ReservaEntity>> = repository.todas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun reservasDe(email: String): Flow<List<ReservaEntity>> = repository.deUsuario(email)

    fun solicitar(dependenciaId: Int, usuarioEmail: String, fecha: String, horaInicio: String, horaFin: String) {
        viewModelScope.launch {
            val error = repository.solicitar(dependenciaId, usuarioEmail, fecha, horaInicio, horaFin)
            _mensaje.value = error ?: "Solicitud enviada"
        }
    }

    fun aprobar(reserva: ReservaEntity) {
        viewModelScope.launch {
            val error = repository.aprobar(reserva)
            if (error == null) {
                NotificacionesHelper.mostrar(
                    getApplication(),
                    reserva.id,
                    "Reserva aprobada",
                    detalle(reserva)
                )
            }
            _mensaje.value = error ?: "Reserva aprobada"
        }
    }

    fun rechazar(reserva: ReservaEntity, motivo: String) {
        viewModelScope.launch {
            repository.rechazar(reserva.id, motivo)
            NotificacionesHelper.mostrar(
                getApplication(),
                reserva.id,
                "Reserva rechazada",
                detalle(reserva)
            )
            _mensaje.value = "Reserva rechazada"
        }
    }

    fun cancelar(id: Int) {
        viewModelScope.launch {
            repository.cancelar(id)
            _mensaje.value = "Reserva cancelada"
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    private fun detalle(reserva: ReservaEntity): String =
        "${Dependencias.nombrePorId(reserva.dependenciaId)} · ${reserva.fecha} · ${reserva.horaInicio} a ${reserva.horaFin}"
}
