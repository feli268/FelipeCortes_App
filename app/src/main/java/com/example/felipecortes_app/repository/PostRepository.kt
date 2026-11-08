package com.example.felipecortes_app.repository

import com.example.felipecortes_app.data.model.Post
import com.example.felipecortes_app.data.remote.RetrofitInstance

class PostRepository {

    suspend fun obtenerPosts(): List<Post> {
        return RetrofitInstance.api.obtenerPosts()
    }

    suspend fun obtenerPostPorId(id: Int): Post {
        return RetrofitInstance.api.obtenerPostPorId(id)
    }

    suspend fun crearPost(post: Post): Post {
        return RetrofitInstance.api.crearPost(post)
    }

    suspend fun actualizarPost(id: Int, post: Post): Post {
        return RetrofitInstance.api.actualizarPost(id, post)
    }

    suspend fun eliminarPost(id: Int) {
        RetrofitInstance.api.eliminarPost(id)
    }
}