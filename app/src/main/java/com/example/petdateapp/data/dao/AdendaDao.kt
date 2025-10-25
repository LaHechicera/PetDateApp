package com.example.petdateapp.data.dao

import androidx.room.*
import com.example.petdateapp.data.entity.AgendaEntity

@Dao
interface AgendaDao {

    // Insertar una cita
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(agenda: AgendaEntity)

    // Eliminar una cita
    @Delete
    suspend fun delete(agenda: AgendaEntity)

    // Obtener todas las citas de un usuario, ordenadas por fecha y hora
    @Query("SELECT * FROM agenda WHERE correoDueno = :correoDueno ORDER BY fecha ASC, hora ASC")
    suspend fun getByUser(correoDueno: String): List<AgendaEntity>

    // Borrar todas las citas de un usuario (opcional)
    @Query("DELETE FROM agenda WHERE correoDueno = :correoDueno")
    suspend fun deleteAllByUser(correoDueno: String)
}
