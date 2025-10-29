package com.example.petdateapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Alfred: DataStore para persistir imágenes (URIs) de la galería por usuario.
 * - Las imágenes se guardan como una cadena CSV.
 * - La clave es dinámica: "imagenes_usuario_<correo>"
 * - No se usa Room, ya que las imágenes no requieren estructura compleja.
 */

// 1. Inicializamos un DataStore específico para la galería
private val Context.galleryDataStore by preferencesDataStore(name = "gallery_prefs")

class GalleryDataStore(private val context: Context) {

    /**
     * Guardar una lista de URIs como CSV para un usuario específico.
     * @param email Correo del usuario logueado
     * @param uris Lista de imágenes (URI) convertidas a String
     */
    suspend fun saveUserGallery(email: String, uris: List<String>) {
        val key = stringPreferencesKey("imagenes_usuario_$email")
        val csv = uris.joinToString(",") // Convertimos lista a CSV

        context.galleryDataStore.edit { prefs ->
            prefs[key] = csv
        }
    }

    /**
     * Obtener la lista persistida de imágenes para el usuario actual.
     * @param email Correo del usuario logueado
     * @return Flow<List<String>> con las URIs almacenadas
     */
    fun getUserGallery(email: String): Flow<List<String>> {
        val key = stringPreferencesKey("imagenes_usuario_$email")

        return context.galleryDataStore.data
            .map { prefs ->
                val csv = prefs[key] ?: ""
                if (csv.isBlank()) emptyList() else csv.split(",")
            }
    }

    /**
     * Obtener la lista de imágenes de forma inmediata (no Flow)
     * Se usa cuando necesitamos los datos una sola vez (ej: al iniciar ViewModel)
     */
    suspend fun getUserGalleryOnce(email: String): List<String> {
        val key = stringPreferencesKey("imagenes_usuario_$email")
        val csv = context.galleryDataStore.data
            .map { prefs -> prefs[key] ?: "" }
            .first()

        return if (csv.isBlank()) emptyList() else csv.split(",")
    }
}
