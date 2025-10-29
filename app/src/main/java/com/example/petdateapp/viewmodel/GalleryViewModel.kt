package com.example.petdateapp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petdateapp.data.datastore.GalleryDataStore
import com.example.petdateapp.data.datastore.UserDataStore
import com.example.petdateapp.data.database.GalleryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel de Galería con Persistencia de Datos.
 * - Mantiene las imágenes en memoria para la UI.
 * - Sincroniza automáticamente con DataStore según el usuario.
 * - Requiere inicialización con contexto para acceder al DataStore.
 */

class GalleryViewModel : ViewModel() {

    // Estado observable por Compose (SE MANTIENE)
    private val _images = mutableStateListOf<Uri>()
    val images: List<Uri> get() = _images

    // Límite máximo de imágenes
    private val maxItems = 6

    // Dependencias
    private var userDataStore: UserDataStore? = null
    private var galleryRepository: GalleryRepository? = null

    private var userEmail: String? = null // Correo del usuario activo

    /**
     * Inicializar DataStore y cargar imágenes del usuario.
     * Llamar esta función desde el Composable o desde NavHost apenas se crea el ViewModel.
     */
    fun init(context: Context) {
        if (userDataStore == null) {
            userDataStore = UserDataStore(context)
        }
        if (galleryRepository == null) {
            val dataStore = GalleryDataStore(context)
            galleryRepository = GalleryRepository(dataStore)
        }

        // Cargar correo del usuario y luego cargar imágenes persistidas
        viewModelScope.launch {
            userEmail = userDataStore?.userEmailFlow?.first()

            // Si no hay usuario guardado, no continuamos
            val email = userEmail ?: return@launch

            // Obtener las imágenes almacenadas y convertirlas a URI
            val savedUris = galleryRepository?.getUserGalleryOnce(email) ?: emptyList()
            _images.clear()
            _images.addAll(savedUris.map { Uri.parse(it) })
        }
    }

    // === Lógica existente para manejar imágenes ===

    fun canAddMore(): Boolean = _images.size < maxItems
    fun remainingSlots(): Int = (maxItems - _images.size).coerceAtLeast(0)


    // Agrega imágenes respetando: Máximo de 6 y Sin duplicados

    fun addImages(newUris: List<Uri>) {
        if (newUris.isEmpty() || !canAddMore()) return

        val deduped = newUris.filterNot { it in _images }
        val take = deduped.take(remainingSlots())

        _images.addAll(take)
        persistGallery() // 🗂 Guardar en DataStore
    }

    //Quita una imagen por índice (si existe).
    fun removeAt(index: Int) {
        if (index in _images.indices) {
            _images.removeAt(index)
            persistGallery() // 🗂 Guardar en DataStore
        }
    }

    //Limpia todas las imágenes (opcional).
    fun clearAll() {
        _images.clear()
        persistGallery() // 🗂 Guardar en DataStore
    }

    /**
     * Guardar el estado actual de la galería en DataStore.
     */
    private fun persistGallery() {
        val email = userEmail ?: return // Si no hay usuario, no se guarda
        val urisAsString = _images.map { it.toString() }

        viewModelScope.launch {
            galleryRepository?.saveUserGallery(email, urisAsString)
        }
    }
}
