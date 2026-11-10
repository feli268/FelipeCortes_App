package com.example.felipecortes_app.viewmodel

import com.example.felipecortes_app.data.model.Post
import com.example.felipecortes_app.repository.PostRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class PostViewModelTest : FunSpec({

    val dispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(dispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    test("al crearse, carga los posts desde el repository") {
        val repositorioFalso = mockk<PostRepository>()
        val postsDePrueba = listOf(
            Post(userId = 1, id = 1, title = "Título", body = "Cuerpo")
        )
        coEvery { repositorioFalso.obtenerPosts() } returns postsDePrueba

        val viewModel = PostViewModel(repository = repositorioFalso)
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.posts.value shouldBe postsDePrueba
        viewModel.cargando.value shouldBe false
    }

    test("si el repository falla, guarda un mensaje de error") {
        val repositorioFalso = mockk<PostRepository>()
        coEvery { repositorioFalso.obtenerPosts() } throws RuntimeException("Sin conexión")

        val viewModel = PostViewModel(repository = repositorioFalso)
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.error.value shouldBe "No se pudieron cargar los posts: Sin conexión"
    }
})