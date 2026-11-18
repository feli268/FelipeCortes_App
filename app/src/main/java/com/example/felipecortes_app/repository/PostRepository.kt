package com.example.felipecortes_app.repository

import com.example.felipecortes_app.data.local.PostDao
import com.example.felipecortes_app.data.local.PostEntity
import com.example.felipecortes_app.data.model.Post
import com.example.felipecortes_app.data.remote.ApiService
import com.example.felipecortes_app.data.remote.RetrofitInstance

class PostRepository(
    private val api: ApiService = RetrofitInstance.api,
    private val dao: PostDao? = null
) {

    suspend fun obtenerPosts(): List<Post> {
        return try {
            val postsDeInternet = api.obtenerPosts()
            dao?.insertarTodos(postsDeInternet.map { it.aEntidad() })
            postsDeInternet
        } catch (e: Exception) {
            val postsGuardados = dao?.obtenerTodos()
            if (postsGuardados != null && postsGuardados.isNotEmpty()) {
                postsGuardados.map { it.aPost() }
            } else {
                throw e
            }
        }
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

private fun Post.aEntidad(): PostEntity {
    return PostEntity(id = id, userId = userId, title = title, body = body)
}

private fun PostEntity.aPost(): Post {
    return Post(id = id, userId = userId, title = title, body = body)
}