package com.example.felipecortes_app.data.remote

import com.example.felipecortes_app.data.model.Post
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("posts")
    suspend fun obtenerPosts(): List<Post>

    @GET("posts/{id}")
    suspend fun obtenerPostPorId(@Path("id") id: Int): Post

    @POST("posts")
    suspend fun crearPost(@Body post: Post): Post

    @PUT("posts/{id}")
    suspend fun actualizarPost(@Path("id") id: Int, @Body post: Post): Post

    @DELETE("posts/{id}")
    suspend fun eliminarPost(@Path("id") id: Int)
}