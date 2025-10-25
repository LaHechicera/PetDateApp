package com.example.petdateapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla: mascotas
// Relación 1 usuario (correo) : N mascotas (correoDueno)
@Entity(tableName = "mascotas")
data class MascotaEntity(
    @PrimaryKey(autoGenerate = true)
    val idMascota: Int = 0,           // ID autogenerado por Room

    // Datos de la mascota (mapeados desde tu RegisterViewModel):
    val nombreMascota: String,
    val especie: String,
    val raza: String,
    val edad: Int,                    // convertir desde String antes de guardar
    val peso: Float,                  // convertir desde String antes de guardar

    // Clave foránea lógica (string) apuntando al correo del dueño:
    val correoDueno: String           // debe existir en usuarios.correo
)
