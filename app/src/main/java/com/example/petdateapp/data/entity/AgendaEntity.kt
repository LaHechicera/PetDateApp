package com.example.petdateapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Alfred: Entidad Agenda para Room
 * - Guarda citas asociadas a un usuario específico
 * - Usa formato ISO para fecha y hora
 */
@Entity(tableName = "agenda")
data class AgendaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,                // ID único de la cita

    val correoDueno: String,         // ← Clave foránea lógica: viene de UsuarioEntity.correo

    val fecha: String,               // Formato ISO: "2025-11-04"
    val hora: String,                // Formato ISO: "14:30"

    val description: String          // Descripción de la cita
)
