package com.example.felipecortes_app.repository

import com.example.felipecortes_app.data.model.Rol
import com.example.felipecortes_app.data.model.Usuario

class AuthRepository {

    private val usuarios = listOf(
        Usuario(email = "vecino@jdvzaror.cl", password = "1234", rol = Rol.VECINO, nombre = "Juan Pérez"),
        Usuario(email = "directiva@jdvzaror.cl", password = "directiva123", rol = Rol.DIRECTIVA, nombre = "Directiva JDV"),
        Usuario(email = "tesoreria@jdvzaror.cl", password = "tesoreria123", rol = Rol.TESORERIA, nombre = "Tesorería JDV"),
        Usuario(email = "admin@jdvzaror.cl", password = "admin123", rol = Rol.ADMIN, nombre = "Administrador")
    )

    fun iniciarSesion(email: String, password: String): Usuario? {
        return usuarios.find { it.email == email && it.password == password }
    }

    fun recuperarContrasena(email: String): Boolean {
        return usuarios.any { it.email == email }
    }
}
