package com.example.petdateapp.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petdateapp.data.database.AppDatabase
import com.example.petdateapp.data.datastore.UserDataStore
import com.example.petdateapp.data.entity.AgendaEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicLong

/**
 * ViewModel de Agenda con persistencia en Room por usuario.
 * - Carga automáticamente las citas del usuario al iniciar.
 * - Guarda y elimina citas en la base de datos local.
 * - Mantiene la lista observable compatible con la UI actual.
 */
class AgendaViewModel : ViewModel() {

    // Modelo usado por la UI (igual que tenías)
    data class AgendaItem(
        val id: Long,
        val dateTime: LocalDateTime,
        val description: String
    )

    private val _items = mutableStateListOf<AgendaItem>()
    val items: List<AgendaItem> get() = _items

    // ROOM + DATASTORE
    private var database: AppDatabase? = null
    private var dataStore: UserDataStore? = null
    private var userEmail: String? = null // Se cargará desde DataStore

    // Inicializar BD y cargar citas automáticamente
    fun init(context: Context) {
        if (database == null) {
            database = AppDatabase.getInstance(context)
        }
        if (dataStore == null) {
            dataStore = UserDataStore(context)
        }

        // Cargar citas del usuario
        viewModelScope.launch {
            // Obtener correo desde DataStore
            userEmail = dataStore?.userEmailFlow?.first()

            if (userEmail != null) {
                loadAppointmentsFromDB()
            }
        }
    }

    /**
     * Cargar citas desde la base de datos Room según el correo del usuario logueado.
     */
    private suspend fun loadAppointmentsFromDB() {
        val db = database ?: return
        val correo = userEmail ?: return

        val agendaList = db.agendaDao().getByUser(correo)

        // Limpiar estado actual y agregar registros desde Room
        _items.clear()
        agendaList.forEach { entity ->
            _items.add(
                AgendaItem(
                    id = entity.id,
                    dateTime = LocalDateTime.of(
                        LocalDate.parse(entity.fecha),
                        LocalTime.parse(entity.hora)
                    ),
                    description = entity.description
                )
            )
        }

        // Mantener las citas ordenadas
        _items.sortBy { it.dateTime }
    }

    /**
     * Agregar cita: guarda en Room y actualiza lista.
     */
    fun addAppointment(date: LocalDate, time: LocalTime, description: String) {
        val correo = userEmail ?: return
        val desc = description.trim().ifEmpty { "Cita veterinaria" }
        val dt = LocalDateTime.of(date, time)

        val entity = AgendaEntity(
            correoDueno = correo,
            fecha = date.toString(), // formato ISO
            hora = time.toString(),
            description = desc
        )

        viewModelScope.launch {
            val db = database ?: return@launch
            db.agendaDao().insert(entity)

            // 🆕 Volver a cargar desde Room para mantener IDs correctos
            loadAppointmentsFromDB()
        }
    }

    /**
     * Eliminar cita desde base de datos.
     */
    fun removeById(id: Long) {
        viewModelScope.launch {
            val db = database ?: return@launch
            val correo = userEmail ?: return@launch

            // Buscar la entidad según id
            val entityList = db.agendaDao().getByUser(correo)
            val entity = entityList.find { it.id == id }

            if (entity != null) {
                db.agendaDao().delete(entity)
                loadAppointmentsFromDB()
            }
        }
    }
}
