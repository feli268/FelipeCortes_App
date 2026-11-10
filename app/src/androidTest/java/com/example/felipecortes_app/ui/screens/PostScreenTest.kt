package com.example.felipecortes_app.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import com.example.felipecortes_app.data.model.Post
import com.example.felipecortes_app.data.remote.ApiService
import com.example.felipecortes_app.repository.PostRepository
import com.example.felipecortes_app.viewmodel.PostViewModel
import org.junit.Rule
import org.junit.Test

private class ApiServiceFalso(private val posts: List<Post>) : ApiService {
    override suspend fun obtenerPosts(): List<Post> = posts
    override suspend fun obtenerPostPorId(id: Int): Post = posts.first { it.id == id }
    override suspend fun crearPost(post: Post): Post = post
    override suspend fun actualizarPost(id: Int, post: Post): Post = post
    override suspend fun eliminarPost(id: Int) {}
}

class PostScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun muestraElTituloDelPostCuandoCargaBien() {
        val postsDePrueba = listOf(
            Post(userId = 1, id = 1, title = "Título visible en pantalla", body = "Cuerpo de prueba")
        )
        val repository = PostRepository(api = ApiServiceFalso(postsDePrueba))
        val viewModel = PostViewModel(repository = repository)

        composeTestRule.setContent {
            PostScreen(viewModel = viewModel)
        }

        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            composeTestRule
                .onAllNodesWithText("Título visible en pantalla")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeTestRule.onNodeWithText("Título visible en pantalla").assertIsDisplayed()
    }
}