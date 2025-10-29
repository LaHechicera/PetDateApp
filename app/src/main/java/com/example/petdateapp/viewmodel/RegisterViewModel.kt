package com.example.petdateapp.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petdateapp.data.database.AppDatabase
import com.example.petdateapp.data.entity.MascotaEntity
import com.example.petdateapp.data.entity.UsuarioEntity
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    // Variables del formulario para mascota
    val nombreMascota = mutableStateOf("")
    val especie = mutableStateOf("")
    val raza = mutableStateOf("")
    val edad = mutableStateOf("")
    val peso = mutableStateOf("")

    // Variables del formulario para dueño
    val nombreDueno = mutableStateOf("")
    val telefono = mutableStateOf("")
    val correo = mutableStateOf("")
    val contrasena = mutableStateOf("")

    // Mensaje de validación
    val mensaje = mutableStateOf("")

    // ROOM-DB: Variables para acceder a la BD
    private var database: AppDatabase? = null

    // ROOM-DB: Esta función debe llamarse desde RegisterScreen para inicializar la BD
    fun initDB(context: Context) {
        if (database == null) {
            database = AppDatabase.getInstance(context)
        }
    }

    // Función principal: valida y guarda en BD si estan correcto
    fun registrar() {
        validar() // Llamamos primero a tu validación original

        // Si el mensaje indica éxito, procedemos con la BD
        if (mensaje.value == "Datos registrados correctamente.") {
            guardarEnBD()
        }
    }

    // Función para validar campos
    fun validar() {
        mensaje.value = when {
            // Validar campos vacíos incluyendo contraseña
            nombreMascota.value.isBlank() || especie.value.isBlank() ||
                    edad.value.isBlank() || peso.value.isBlank() ||
                    nombreDueno.value.isBlank() || telefono.value.isBlank() ||
                    correo.value.isBlank() || contrasena.value.isBlank() ->
                "Completa todos los campos"

            //Validación de correo
            !correo.value.contains("@") ->
                "Correo inválido, debes agregar un @"

            //Validación de contraseña
            contrasena.value.length < 6 ->
                "La contraseña debe tener al menos 6 caracteres"

            // Validación de teléfono (solo números)
            !telefono.value.matches(Regex("^[0-9]+$")) ->
                "El teléfono debe contener solo números"

            // Validación de longitud mínima del teléfono
            telefono.value.length < 8 ->
                "El teléfono debe tener al menos 8 dígitos"

            //Validación de edad
            edad.value.toIntOrNull() == null ->
                "La edad debe ser un número entero"

            //Validación de peso
            peso.value.toFloatOrNull() == null ->
                "El peso debe ser un número, con o sin coma"

            else -> "Datos registrados correctamente."
        }
    }

    // ROOM-DB: Guardar Usuario y Mascota en la base de datos
    private fun guardarEnBD() {
        val db = database
        if (db == null) {
            mensaje.value = "Error interno: BD no inicializada"
            return
        }

        viewModelScope.launch {
            try {
                // 1) Crear entidad Usuario
                val usuarioEntity = UsuarioEntity(
                    correo = correo.value,
                    nombreDueno = nombreDueno.value,
                    telefono = telefono.value,
                    contrasena = contrasena.value
                )

                // Insertar usuario
                db.usuarioDao().insert(usuarioEntity)

                // 2) Crear entidad Mascota
                val mascotaEntity = MascotaEntity(
                    nombreMascota = nombreMascota.value,
                    especie = especie.value,
                    raza = raza.value,
                    edad = edad.value.toInt(),
                    peso = peso.value.toFloat(),
                    correoDueno = correo.value
                )

                // Insertar mascota
                db.mascotaDao().insert(mascotaEntity)

                // Actualizamos el mensaje para la UI (éxito)
                mensaje.value = "Datos registrados correctamente."

            } catch (e: Exception) {
                // Si el correo ya existe u otro error de BD
                mensaje.value = "Error al registrar: ${e.message}"
            }
        }
    }
}
