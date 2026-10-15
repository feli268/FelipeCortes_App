package com.example.felipecortes_app.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Crea (una sola vez, a nivel de Context) el DataStore donde se guardan los ajustes simples.
val Context.dataStore by preferencesDataStore(name = "ajustes")

// La "llave" bajo la cual se guarda nuestro valor true/false.
private val MODO_ESPECIAL_KEY = booleanPreferencesKey("modo_especial")

object AjustesRepository {

    // Devuelve un Flow que emite el valor guardado cada vez que cambia (false si nunca se ha guardado nada).
    fun leerModoEspecial(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferencias ->
            preferencias[MODO_ESPECIAL_KEY] ?: false
        }
    }

    suspend fun guardarModoEspecial(context: Context, valor: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[MODO_ESPECIAL_KEY] = valor
        }
    }
}