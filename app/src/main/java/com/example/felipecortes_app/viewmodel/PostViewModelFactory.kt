package com.example.felipecortes_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.felipecortes_app.data.local.AppDatabase
import com.example.felipecortes_app.repository.PostRepository

class PostViewModelFactory(private val dao: com.example.felipecortes_app.data.local.PostDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return PostViewModel(repository = PostRepository(dao = dao)) as T
    }
}