package com.example.petdateapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LogInViewModel: ViewModel(){

    val correo = mutableStateOf("")
    val clave = mutableStateOf("")
    val mensaje = mutableStateOf("")

    fun validar(){
        mensaje.value = when{
            correo.value.isBlank() || clave.value.isBlank() ->
                "Completa todos los campos"

            !correo.value.contains("@") ->
                "Correo invalido, debe contener @"

            else -> "Ingreso sesión exitosa"
        }
    }
}