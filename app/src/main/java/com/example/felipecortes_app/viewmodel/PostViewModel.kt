package com.example.felipecortes_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.felipecortes_app.data.model.Post
import com.example.felipecortes_app.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostViewModel(
    private val repository: PostRepository = PostRepository()
) : ViewModel() {

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        cargarPosts()
    }

    fun cargarPosts() {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            try {
                _posts.value = repository.obtenerPosts()
            } catch (e: Exception) {
                _error.value = "No se pudieron cargar los posts: ${e.message}"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun crearPost(post: Post) {
        viewModelScope.launch {
            try {
                repository.crearPost(post)
                cargarPosts()
            } catch (e: Exception) {
                _error.value = "No se pudo crear el post: ${e.message}"
            }
        }
    }

    fun actualizarPost(id: Int, post: Post) {
        viewModelScope.launch {
            try {
                repository.actualizarPost(id, post)
                cargarPosts()
            } catch (e: Exception) {
                _error.value = "No se pudo actualizar el post: ${e.message}"
            }
        }
    }

    fun eliminarPost(id: Int) {
        viewModelScope.launch {
            try {
                repository.eliminarPost(id)
                cargarPosts()
            } catch (e: Exception) {
                _error.value = "No se pudo eliminar el post: ${e.message}"
            }
        }
    }
}