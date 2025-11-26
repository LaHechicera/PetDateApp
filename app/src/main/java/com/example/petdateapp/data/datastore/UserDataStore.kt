package com.example.petdateapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Alfred: DataStore para guardar datos del usuario de forma persistente.
 * - Guardaremos el correo del usuario que inició sesión.
 * - Esto permite que otras pantallas (como Agenda) sepan qué usuario está activo.
 */

// 1. Inicializamos DataStore (Singleton) a nivel de Context y lo hacemos 'internal'
//    para que sea visible por otras clases como UserManager.
internal val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserDataStore(private val context: Context) {

    // 2. Clave para almacenar el correo
    companion object {
        val USER_EMAIL_KEY = stringPreferencesKey("user_email")    }

    // 3. Función para guardar el correo en DataStore
    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_EMAIL_KEY] = email
        }
    }

    // 4. Flujo para obtener el correo almacenado (Flow = datos en vivo)
    val userEmailFlow: Flow<String?> = context.dataStore.data
        .map { prefs ->
            prefs[USER_EMAIL_KEY] // Retorna null si no existe
        }

    // 5. Opción para limpiar el correo al cerrar sesión
    suspend fun clearUserEmail() {
        context.dataStore.edit { prefs ->
            prefs.remove(USER_EMAIL_KEY)
        }
    }
}
