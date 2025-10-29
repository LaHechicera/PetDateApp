package com.example.petdateapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.petdateapp.data.dao.AgendaDao // ✅ IMPORTAR NUEVO DAO
import com.example.petdateapp.data.dao.MascotaDao
import com.example.petdateapp.data.dao.UsuarioDao
import com.example.petdateapp.data.entity.AgendaEntity // ✅ IMPORTAR NUEVA ENTIDAD
import com.example.petdateapp.data.entity.MascotaEntity
import com.example.petdateapp.data.entity.UsuarioEntity

// ✅ IMPORTANTE: se incrementa la versión de 1 a 2 porque agregamos una tabla nueva
@Database(
    entities = [
        UsuarioEntity::class,
        MascotaEntity::class,
        AgendaEntity::class
    ],
    version = 2, // ⬅ Aquí está el cambio
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    // Exponer los DAO
    abstract fun usuarioDao(): UsuarioDao
    abstract fun mascotaDao(): MascotaDao
    abstract fun agendaDao(): AgendaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Singleton: obtén una única instancia de BD para toda la app
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "petdate_db" // nombre del archivo SQLite
                )
                    // Durante el desarrollo, usar esta línea para no tener errores de migración:
                    .fallbackToDestructiveMigration() //Este hace que se eliminan las bases de datos
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
