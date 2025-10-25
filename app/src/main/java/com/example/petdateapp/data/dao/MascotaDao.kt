package com.example.petdateapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.petdateapp.data.entity.MascotaEntity

@Dao
interface MascotaDao {

    // Insertar una mascota
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(mascota: MascotaEntity)

    // Listar mascotas por correo del dueño
    @Query("SELECT * FROM mascotas WHERE correoDueno = :correoDueno ORDER BY idMascota DESC")
    suspend fun getByDueno(correoDueno: String): List<MascotaEntity>

    // (Opcional) Borrar todas las mascotas de un dueño
    @Query("DELETE FROM mascotas WHERE correoDueno = :correoDueno")
    suspend fun deleteByDueno(correoDueno: String)
}
