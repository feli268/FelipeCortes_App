package com.example.felipecortes_app.repository

import com.example.felipecortes_app.data.model.Post
import com.example.felipecortes_app.data.remote.ApiService
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class PostRepositoryTest : FunSpec({

    test("obtenerPosts devuelve la lista que entrega la API") {
        val apiFalsa = mockk<ApiService>()
        val postsDePrueba = listOf(
            Post(userId = 1, id = 1, title = "Título de prueba", body = "Cuerpo de prueba")
        )
        coEvery { apiFalsa.obtenerPosts() } returns postsDePrueba

        val repository = PostRepository(api = apiFalsa)
        val resultado = repository.obtenerPosts()

        resultado shouldBe postsDePrueba
    }

    test("eliminarPost llama a la API con el id correcto") {
        val apiFalsa = mockk<ApiService>()
        coEvery { apiFalsa.eliminarPost(5) } returns Unit

        val repository = PostRepository(api = apiFalsa)
        repository.eliminarPost(5)

        coVerify { apiFalsa.eliminarPost(5) }
    }
})