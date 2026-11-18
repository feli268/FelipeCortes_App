package com.example.felipecortes_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.felipecortes_app.data.local.AppDatabase
import com.example.felipecortes_app.data.local.ComprobanteEntity
import com.example.felipecortes_app.repository.ComprobanteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ComprobanteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ComprobanteRepository(
        AppDatabase.obtenerInstancia(application).comprobanteDao()
    )

    val comprobantes: StateFlow<List<ComprobanteEntity>> = repository.comprobantes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun adjuntar(movimientoId: Int, nombreArchivo: String, uriLocal: String) {
        viewModelScope.launch {
            val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
            val error = repository.adjuntar(movimientoId, nombreArchivo, uriLocal, fecha)
            _mensaje.value = error ?: "Comprobante adjuntado"
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
