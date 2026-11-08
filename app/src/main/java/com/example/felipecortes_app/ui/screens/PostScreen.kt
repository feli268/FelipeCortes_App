package com.example.felipecortes_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.felipecortes_app.data.model.Post
import com.example.felipecortes_app.viewmodel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    viewModel: PostViewModel = viewModel()
) {
    val posts by viewModel.posts.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Posts") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.crearPost(
                    Post(userId = 1, id = 0, title = "Post de prueba", body = "Creado desde la app")
                )
            }) {
                Icon(Icons.Default.Add, contentDescription = "Crear post de prueba")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when {
                cargando -> {
                    CircularProgressIndicator()
                }
                error != null -> {
                    Text(text = error ?: "Error desconocido")
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(posts) { post ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = post.title, style = MaterialTheme.typography.titleMedium)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = post.body, style = MaterialTheme.typography.bodyMedium)
                                    }
                                    IconButton(onClick = { viewModel.eliminarPost(post.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar post")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}