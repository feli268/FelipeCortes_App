package com.example.felipecortes_app.repository

import com.example.felipecortes_app.data.model.Rol
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class AuthRepositoryTest : FunSpec({

    val repository = AuthRepository()

    test("login de vecino con datos correctos devuelve el rol VECINO") {
        val usuario = repository.iniciarSesion("vecino@jdvzaror.cl", "1234")
        usuario.shouldNotBeNull()
        usuario?.rol shouldBe Rol.VECINO
    }

    test("cada perfil inicia sesión con su propio rol") {
        repository.iniciarSesion("directiva@jdvzaror.cl", "directiva123")?.rol shouldBe Rol.DIRECTIVA
        repository.iniciarSesion("tesoreria@jdvzaror.cl", "tesoreria123")?.rol shouldBe Rol.TESORERIA
        repository.iniciarSesion("admin@jdvzaror.cl", "admin123")?.rol shouldBe Rol.ADMIN
    }

    test("contraseña incorrecta no inicia sesión") {
        repository.iniciarSesion("vecino@jdvzaror.cl", "mala").shouldBeNull()
    }

    test("correo inexistente no inicia sesión") {
        repository.iniciarSesion("nadie@x.cl", "1234").shouldBeNull()
    }

    test("recuperar contraseña solo funciona con correos registrados") {
        repository.recuperarContrasena("vecino@jdvzaror.cl") shouldBe true
        repository.recuperarContrasena("nadie@x.cl") shouldBe false
    }
})
