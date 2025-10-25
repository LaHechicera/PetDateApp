package com.example.petdateapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.petdateapp.viewmodel.LogInViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LogInViewModel = viewModel()
) {
    var showWelcomeDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Inicializar la base de datos una vez
    LaunchedEffect(Unit) {
        viewModel.initDB(context)
    }

    // Escuchar cambios en el mensaje del ViewModel
    LaunchedEffect(viewModel.mensaje.value) {
        if (viewModel.mensaje.value == "Ingreso sesión exitosa") {
            showWelcomeDialog = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Campo de correo
        OutlinedTextField(
            value = viewModel.correo.value,
            onValueChange = { viewModel.correo.value = it },
            label = { Text("Correo") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Campo de contraseña
        OutlinedTextField(
            value = viewModel.clave.value,
            onValueChange = { viewModel.clave.value = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Botón de inicio de sesión (solo llama a validar)
        Button(
            onClick = {
                viewModel.validar() // Aquí se hace la validación y consulta en BD
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Iniciar Sesión", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Mensaje (error o éxito)
        Text(
            viewModel.mensaje.value,
            color = if (viewModel.mensaje.value == "Ingreso sesión exitosa")
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    // Popup de bienvenida y navegación automática
    if (showWelcomeDialog) {
        AlertDialog(
            onDismissRequest = { /* Evitar cerrar manualmente */ },
            confirmButton = {},
            title = {
                Text(
                    "¡Bienvenido a PetDate!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Text(
                    "Nos alegra verte nuevamente 🐾",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            containerColor = MaterialTheme.colorScheme.surface
        )

        //  Retraso y navegación
        LaunchedEffect(Unit) {
            delay(2000) // tiempo del popup
            showWelcomeDialog = false
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }
}

