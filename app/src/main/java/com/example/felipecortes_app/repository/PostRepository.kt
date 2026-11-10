package com.example.felipecortes_app.repository

import com.example.felipecortes_app.data.model.Post
import com.example.felipecortes_app.data.remote.ApiService
import com.example.felipecortes_app.data.remote.RetrofitInstance

class PostRepository(
    private val api: ApiService = RetrofitInstance.api
) {

    suspend fun obtenerPosts(): List<Post> {
        return api.obtenerPosts()
    }

    suspend fun obtenerPostPorId(id: Int): Post {
        return api.obtenerPostPorId(id)
    }

    suspend fun crearPost(post: Post): Post {
        return api.crearPost(post)
    }

    suspend fun actualizarPost(id: Int, post: Post): Post {
        return api.actualizarPost(id, post)
    }

    suspend fun eliminarPost(id: Int) {
        api.eliminarPost(id)
    }
}