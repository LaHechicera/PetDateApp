package com.example.petdateapp

import com.example.petdateapp.viewmodel.RegisterViewModel
import org.junit.Assert.*
import org.junit.Test

class RegisterViewModelTest {

    private val viewModel = RegisterViewModel()

    @Test
    fun camposVacios_devuelveError() {
        viewModel.nombreDueno.value = ""
        viewModel.telefono.value = ""
        viewModel.correo.value = ""
        viewModel.contrasena.value = ""

        viewModel.validar()

        assertEquals("Por favor, completa todos los campos.", viewModel.mensaje.value)
    }

    @Test
    fun correoSinArroba_devuelveError() {
        viewModel.nombreDueno.value = "Camila"
        viewModel.telefono.value = "98765432"
        viewModel.correo.value = "camila.test.com"
        viewModel.contrasena.value = "123456"

        viewModel.validar()

        assertEquals("Correo inválido, debes agregar un @", viewModel.mensaje.value)
    }

    @Test
    fun contrasenaCorta_devuelveError() {
        viewModel.nombreDueno.value = "Camila"
        viewModel.telefono.value = "98765432"
        viewModel.correo.value = "camila@test.com"
        viewModel.contrasena.value = "123"

        viewModel.validar()

        assertEquals("La contraseña debe tener al menos 6 caracteres", viewModel.mensaje.value)
    }

    @Test
    fun telefonoConLetra_devuelveError() {
        viewModel.nombreDueno.value = "Camila"
        viewModel.telefono.value = "98765a32"
        viewModel.correo.value = "camila@test.com"
        viewModel.contrasena.value = "123456"

        viewModel.validar()

        assertEquals("El teléfono debe contener solo números", viewModel.mensaje.value)
    }

    @Test
    fun telefonoCorto_devuelveError() {
        viewModel.nombreDueno.value = "Camila"
        viewModel.telefono.value = "12345"
        viewModel.correo.value = "camila@test.com"
        viewModel.contrasena.value = "123456"

        viewModel.validar()

        assertEquals("El teléfono debe tener al menos 8 dígitos", viewModel.mensaje.value)
    }

    @Test
    fun datosValidos_Devuelve() {
        viewModel.nombreDueno.value = "Camila"
        viewModel.telefono.value = "98765432"
        viewModel.correo.value = "camila@test.com"
        viewModel.contrasena.value = "123456"

        viewModel.validar()

        assertEquals("Datos registrados correctamente.", viewModel.mensaje.value)
    }
}