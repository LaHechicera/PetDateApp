package com.example.petdateapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.petdateapp.data.dao.AgendaDao 
import com.example.petdateapp.data.dao.MascotaDao
import com.example.petdateapp.data.dao.UsuarioDao
import com.example.petdateapp.data.entity.AgendaEntity 
import com.example.petdateapp.data.entity.MascotaEntity
import com.example.petdateapp.data.entity.UsuarioEntity

@Database(
    entities = [
        UsuarioEntity::class,
        MascotaEntity::class,
        AgendaEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun mascotaDao(): MascotaDao
    abstract fun agendaDao(): AgendaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "petdate_db"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() 
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
