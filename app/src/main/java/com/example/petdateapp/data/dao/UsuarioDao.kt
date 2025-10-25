package com.example.petdateapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.petdateapp.data.entity.UsuarioEntity

@Dao
interface UsuarioDao {

    // Insertar usuario; si el correo ya existe, fallará o reemplazará según estrategia.
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(usuario: UsuarioEntity)

    // Login básico: busca por correo y contraseña
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contrasena = :contrasena LIMIT 1")
    suspend fun login(correo: String, contrasena: String): UsuarioEntity?

    // Buscar usuario por correo
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun findByCorreo(correo: String): UsuarioEntity?
}
