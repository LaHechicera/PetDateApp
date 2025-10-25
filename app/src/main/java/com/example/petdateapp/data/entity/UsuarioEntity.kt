package com.example.petdateapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabla: usuarios
// Basada en tu RegisterViewModel (correo único como PK)
@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = false) // correo es único y actúa como ID
    val correo: String,               // para login y relación 1:N con mascotas

    // Datos del dueño:
    val nombreDueno: String,
    val telefono: String,
    val contrasena: String            // solicitado: contraseña simple (texto)
)
