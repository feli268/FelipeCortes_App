package com.example.felipecortes_app.data.model

enum class Rol {
    VECINO,
    DIRECTIVA,
    TESORERIA,
    ADMIN
}

data class Usuario(
    val email: String,
    val password: String,
    val rol: Rol,
    val nombre: String
)
