package com.example.petdateapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row // <-- agregado para la fila del texto de registro
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton // <-- agregado para el enlace "Regístrate"
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

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.size
import com.example.petdateapp.R

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LogInViewModel = viewModel()
) {
    var showWelcomeDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Detectar tema del sistema
    val isDarkTheme = isSystemInDarkTheme() // Detectar tema oscuro o claro
    val logoRes = if (isDarkTheme) com.example.petdateapp.R.drawable.logo_dark else R.drawable.logo_light // Seleccionar logo según tema

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
        verticalArrangement = Arrangement.Top //Para mostrar logo arriba
    ) {

        //Logo
        Image(
            painter = painterResource(id = logoRes),
            contentDescription = "Logo PetDate",
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp) //Mueve el logo hacia abajo
                .size(160.dp), //Aumenta tamaño del logo
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(30.dp))

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

        // 👉 NUEVO: enlace para ir a la pantalla de registro debajo del botón de inicio de sesión
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¿No tienes sesión?",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
            TextButton(
                onClick = {
                    // Navegar a la pantalla de registro
                    navController.navigate("registro")
                }
            ) {
                Text(
                    text = "Regístrate",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
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
            onDismissRequest = {}, //Evitar cerrar manualmente
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
