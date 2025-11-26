package com.example.petdateapp.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.petdateapp.data.datastore.dataStore // Importa la instancia ÚNICA y compartida
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID

// Se ha ELIMINADO la definición duplicada de `preferencesDataStore` que causaba el conflicto.

class UserManager(private val context: Context) {

    private object PreferencesKeys {
        val USER_ID = stringPreferencesKey("user_id")
    }

    /**
     * Obtiene el ID de usuario único del DataStore.
     * Si no existe, genera uno nuevo, lo guarda y lo retorna.
     */
    suspend fun getUserId(): String {
        // Lee el ID de usuario desde la instancia compartida de DataStore.
        val userId = context.dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USER_ID]
        }.first()

        // Si es nulo (primera vez que se ejecuta), genera y guarda un nuevo ID.
        return userId ?: generateAndStoreUserId()
    }

    private suspend fun generateAndStoreUserId(): String {
        val newUserId = UUID.randomUUID().toString()
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = newUserId
        }
        return newUserId
    }
}
