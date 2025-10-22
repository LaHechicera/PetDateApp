package com.example.petdateapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RegisterViewModel: ViewModel() {
    val nombreMascota = mutableStateOf("")
    val especie = mutableStateOf("")
    val raza = mutableStateOf("")
    val edad = mutableStateOf("")
    val peso = mutableStateOf("")

    // Variables del formulario - Datos del dueño
    val nombreDueno = mutableStateOf("")
    val telefono = mutableStateOf("")
    val correo = mutableStateOf("")
    val contrasena = mutableStateOf("")

    // Mensaje de validación
    val mensaje = mutableStateOf("")

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

            //Validación de edad numérica
            edad.value.toIntOrNull() == null ->
                "La edad debe ser un número entero"

            //Validación de peso numérico
            peso.value.toFloatOrNull() == null ->
                "El peso debe ser un número válido sin punto"

            else -> "Datos registrados correctamente."
        }
    }
}
