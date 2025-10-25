package com.example.petdateapp.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petdateapp.data.database.AppDatabase
import com.example.petdateapp.data.datastore.UserDataStore
import kotlinx.coroutines.launch

class LogInViewModel : ViewModel() {

    val correo = mutableStateOf("")
    val clave = mutableStateOf("")
    val mensaje = mutableStateOf("")

    // ROOM-DB
    private var database: AppDatabase? = null

    // DATASTORE
    private var dataStore: UserDataStore? = null

    // Inicializamos BD + DataStore
    fun initDB(context: Context) {
        if (database == null) {
            database = AppDatabase.getInstance(context)
        }
        if (dataStore == null) {
            dataStore = UserDataStore(context)
        }
    }

    // Tu función original de validar, adaptada para verificar en Room
    fun validar() {
        mensaje.value = when {
            correo.value.isBlank() || clave.value.isBlank() ->
                "Completa todos los campos"

            !correo.value.contains("@") ->
                "Correo inválido, debe contener @"

            else -> {
                verificarBD()
                "" // Evita mostrar mensaje antes de validar con BD
            }
        }
    }

    // Consulta en Room para verificar si el usuario existe
    private fun verificarBD() {
        val db = database
        val ds = dataStore

        if (db == null) {
            mensaje.value = "Error: base de datos no inicializada"
            return
        }

        if (ds == null) {
            mensaje.value = "Error: DataStore no inicializado"
            return
        }

        viewModelScope.launch {
            val usuario = db.usuarioDao().login(correo.value, clave.value)

            if (usuario != null) {
                // Guardar el correo en DataStore
                ds.saveUserEmail(correo.value)

                // Activar tu flujo original
                mensaje.value = "Ingreso sesión exitosa"
            } else {
                mensaje.value = "Usuario o contraseña incorrectos"
            }
        }
    }
}
