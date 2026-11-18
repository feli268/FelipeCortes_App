package com.example.felipecortes_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.felipecortes_app.data.local.AppDatabase
import com.example.felipecortes_app.data.local.MovimientoEntity
import com.example.felipecortes_app.data.model.TipoMovimiento
import com.example.felipecortes_app.repository.MovimientoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovimientoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovimientoRepository(
        AppDatabase.obtenerInstancia(application).movimientoDao()
    )

    val movimientos: StateFlow<List<MovimientoEntity>> = repository.movimientos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalIngresos: StateFlow<Int> = repository.totalIngresos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalGastos: StateFlow<Int> = repository.totalGastos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val saldo: StateFlow<Int> = combine(repository.totalIngresos, repository.totalGastos) { ingresos, gastos ->
        ingresos - gastos
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun registrar(tipo: TipoMovimiento, categoria: String, descripcion: String, montoTexto: String, fecha: String) {
        viewModelScope.launch {
            val error = repository.registrar(tipo, categoria, descripcion.trim(), montoTexto.trim(), fecha.trim())
            _mensaje.value = error ?: "Movimiento registrado"
        }
    }

    fun anular(id: Int) {
        viewModelScope.launch {
            repository.anular(id)
            _mensaje.value = "Movimiento anulado"
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
