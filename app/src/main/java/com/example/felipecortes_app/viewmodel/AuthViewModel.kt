package com.example.felipecortes_app.viewmodel

import androidx.lifecycle.ViewModel
import com.example.felipecortes_app.data.model.Usuario
import com.example.felipecortes_app.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun iniciarSesion(email: String, password: String) {
        val usuario = repository.iniciarSesion(email, password)
        if (usuario != null) {
            _usuarioActual.value = usuario
            _error.value = null
        } else {
            _error.value = "Correo o contraseña incorrectos"
        }
    }

    fun cerrarSesion() {
        _usuarioActual.value = null
    }

    fun recuperarContrasena(email: String): Boolean {
        return repository.recuperarContrasena(email)
    }
}
