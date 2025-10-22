package com.example.petdateapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import java.time.*
import java.util.concurrent.atomic.AtomicLong
/**
 * Alfred: ViewModel de Agenda
 * - Mantiene una lista observable de citas (máxima simpleza).
 * - Permite agregar y eliminar.
 * - Mantiene la lista SIEMPRE ordenada por fecha/hora ascendente.
 */
class AgendaViewModel : ViewModel() {

    // Modelo de datos sencillo para una cita
    data class AgendaItem(
        val id: Long,
        val dateTime: LocalDateTime,
        val description: String
    )

    private val idGen = AtomicLong(1L)

    // Estado observable por Compose
    private val _items = mutableStateListOf<AgendaItem>()
    val items: List<AgendaItem> get() = _items

    /**
     * Agrega una cita a partir de (fecha local, hora local, descripción).
     * Si la descripción viene en blanco, la normalizamos a un texto corto.
     */
    fun addAppointment(date: LocalDate, time: LocalTime, description: String) {
        val desc = description.trim().ifEmpty { "Cita veterinaria" }
        val dt = LocalDateTime.of(date, time)

        val newItem = AgendaItem(
            id = idGen.getAndIncrement(),
            dateTime = dt,
            description = desc
        )

        _items.add(newItem)
        _items.sortBy { it.dateTime } // Alfred: mantenemos orden natural
    }

    /** Elimina por id (si existe). */
    fun removeById(id: Long) {
        val idx = _items.indexOfFirst { it.id == id }
        if (idx >= 0) _items.removeAt(idx)
    }

    /** Limpia todas las citas (opcional). */
    fun clearAll() = _items.clear()
}
