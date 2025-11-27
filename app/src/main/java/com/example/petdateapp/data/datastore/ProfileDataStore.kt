package com.example.petdateapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.profileDataStore by preferencesDataStore("profile_preferences")

data class ProfileData(
    val nombre: String,
    val telefono: String,
    val genero: String,
    val imagenUri: String?
)

class ProfileDataStore(context: Context) {
    private val dataStore = context.profileDataStore

    private fun userKey(key: String, email: String) = "${key}_$email"

    suspend fun saveProfileData(email: String, nombre: String, telefono: String, genero: String, imagenUri: String?) {
        dataStore.edit { preferences ->
            if (email.isNotBlank()) {
                preferences[stringPreferencesKey(userKey("nombre", email))] = nombre
                preferences[stringPreferencesKey(userKey("telefono", email))] = telefono
                preferences[stringPreferencesKey(userKey("genero", email))] = genero
                preferences[stringPreferencesKey(userKey("imagen_uri", email))] = imagenUri ?: ""
            }
        }
    }

    fun getProfileData(email: String): Flow<ProfileData?> {
        return dataStore.data.map { preferences ->
            if (email.isNotBlank()) {
                ProfileData(
                    nombre = preferences[stringPreferencesKey(userKey("nombre", email))] ?: "",
                    telefono = preferences[stringPreferencesKey(userKey("telefono", email))] ?: "",
                    genero = preferences[stringPreferencesKey(userKey("genero", email))] ?: "",
                    imagenUri = preferences[stringPreferencesKey(userKey("imagen_uri", email))].takeIf { it?.isNotBlank() ?: false }
                )
            } else {
                null
            }
        }
    }

    suspend fun clearProfileData(email: String) {
        dataStore.edit { preferences ->
            if (email.isNotBlank()) {
                preferences.remove(stringPreferencesKey(userKey("nombre", email)))
                preferences.remove(stringPreferencesKey(userKey("telefono", email)))
                preferences.remove(stringPreferencesKey(userKey("genero", email)))
                preferences.remove(stringPreferencesKey(userKey("imagen_uri", email)))
            }
        }
    }
}