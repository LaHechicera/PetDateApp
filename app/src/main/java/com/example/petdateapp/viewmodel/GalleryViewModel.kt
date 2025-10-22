package com.example.petdateapp.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

/**
 * Alfred: ViewModel sencillo para gestionar selección de imágenes.
 * - Mantiene una lista reactiva de Uris (máx. 6).
 * - Evita duplicados.
 * - Expone helpers para saber si se puede agregar más.
 */
class GalleryViewModel : ViewModel() {

    // Estado observable por Compose
    private val _images = mutableStateListOf<Uri>()
    val images: List<Uri> get() = _images

    // Límite máximo
    private val maxItems = 6

    fun canAddMore(): Boolean = _images.size < maxItems
    fun remainingSlots(): Int = (maxItems - _images.size).coerceAtLeast(0)

    /**
     * Agrega imágenes respetando:
     * - Máximo de 6
     * - Sin duplicados
     */
    fun addImages(newUris: List<Uri>) {
        if (newUris.isEmpty() || !canAddMore()) return

        // Filtra duplicados respecto del estado actual
        val deduped = newUris.filterNot { it in _images }

        // Calcula cupo restante y corta la lista si es necesario
        val take = deduped.take(remainingSlots())

        _images.addAll(take)
    }

    /** Quita una imagen por índice (si existe). */
    fun removeAt(index: Int) {
        if (index in _images.indices) _images.removeAt(index)
    }

    /** Limpia todas las imágenes (opcional). */
    fun clearAll() {
        _images.clear()
    }
}
