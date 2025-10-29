package com.example.petdateapp.data.database

import com.example.petdateapp.data.datastore.GalleryDataStore
import kotlinx.coroutines.flow.Flow

/**
 * Alfred: Repositorio de Galería
 * - Actúa como intermediario entre el ViewModel y el DataStore.
 * - Mantiene el principio de separación de responsabilidades (Clean Architecture).
 * - Aquí no se manipulan datos directamente; solo se delega a GalleryDataStore.
 */
class GalleryRepository(private val galleryDataStore: GalleryDataStore) {

    /**
     * Guarda la lista de URIs como CSV para el usuario actual.
     * @param email Correo del usuario logueado
     * @param uris Lista de imágenes a guardar
     */
    suspend fun saveUserGallery(email: String, uris: List<String>) {
        galleryDataStore.saveUserGallery(email, uris)
    }

    /**
     * Recupera la lista persistente de imágenes (modo Flow).
     * @param email Correo del usuario logueado
     * @return Flow<List<String>>
     */
    fun getUserGallery(email: String): Flow<List<String>> {
        return galleryDataStore.getUserGallery(email)
    }

    /**
     * Recupera la lista de imágenes una sola vez (suspend).
     * Ideal para inicializar el estado del ViewModel.
     */
    suspend fun getUserGalleryOnce(email: String): List<String> {
        return galleryDataStore.getUserGalleryOnce(email)
    }
}
