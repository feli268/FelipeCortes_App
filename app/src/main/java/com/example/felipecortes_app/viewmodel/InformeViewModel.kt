package com.example.felipecortes_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.felipecortes_app.data.local.AppDatabase
import com.example.felipecortes_app.data.local.TotalCategoria
import com.example.felipecortes_app.data.model.TipoMovimiento
import com.example.felipecortes_app.repository.MovimientoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class InformeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MovimientoRepository(
        AppDatabase.obtenerInstancia(application).movimientoDao()
    )

    // Período en formato AAAA-MM. Si queda vacío, el informe cubre todo.
    private val _periodo = MutableStateFlow(periodoActual())
    val periodo: StateFlow<String> = _periodo.asStateFlow()

    val ingresos: StateFlow<Int> = _periodo
        .flatMapLatest { repository.totalPorTipoYPeriodo(TipoMovimiento.INGRESO, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val gastos: StateFlow<Int> = _periodo
        .flatMapLatest { repository.totalPorTipoYPeriodo(TipoMovimiento.GASTO, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val saldo: StateFlow<Int> = combine(ingresos, gastos) { i, g -> i - g }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val gastosPorCategoria: StateFlow<List<TotalCategoria>> = _periodo
        .flatMapLatest { repository.gastosPorCategoria(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun cambiarPeriodo(nuevo: String) {
        _periodo.value = nuevo.trim()
    }

    private fun periodoActual(): String =
        SimpleDateFormat("yyyy-MM", Locale.US).format(Date())
}
