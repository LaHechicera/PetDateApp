package com.example.petdateapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    logInViewModel: LogInViewModel
) {
    var showWelcomeDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val isDarkTheme = isSystemInDarkTheme()
    val logoRes = if (isDarkTheme) com.example.petdateapp.R.drawable.logo_dark else R.drawable.logo_light

    LaunchedEffect(Unit) {
        logInViewModel.initDB(context)
    }

    LaunchedEffect(logInViewModel.mensaje.value) {
        if (logInViewModel.mensaje.value == "Ingreso sesión exitosa") {
            showWelcomeDialog = true
        }
    }

    val outlinedTextFieldColors = if (isSystemInDarkTheme()) {
        OutlinedTextFieldDefaults.colors(
            focusedLabelColor = Color(0xFFC2B872),
            unfocusedLabelColor = Color(0xFFC2B872),
            focusedTextColor = Color(0xFFC2B872),
            unfocusedTextColor = Color(0xFFC2B872)
        )
    } else {
        OutlinedTextFieldDefaults.colors()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Image(
            painter = painterResource(id = logoRes),
            contentDescription = "Logo PetDate",
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .size(160.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = logInViewModel.correo.value,
            onValueChange = { logInViewModel.correo.value = it },
            label = { Text("Correo") },
            singleLine = true,
            colors = outlinedTextFieldColors
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = logInViewModel.clave.value,
            onValueChange = { logInViewModel.clave.value = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            colors = outlinedTextFieldColors
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                logInViewModel.validar()
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

        Text(
            logInViewModel.mensaje.value,
            color = if (logInViewModel.mensaje.value == "Ingreso sesión exitosa")
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium
        )
    }

    if (showWelcomeDialog) {
        val titleColor = if (isSystemInDarkTheme()) Color(0xFF120B06) else MaterialTheme.colorScheme.primary
        val textColor = if (isSystemInDarkTheme()) Color(0xFF120B06) else MaterialTheme.colorScheme.onSurface
        AlertDialog(
            onDismissRequest = {}, 
            confirmButton = {},
            title = {
                Text(
                    "¡Bienvenido a PetDate!",
                    color = titleColor,
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Text(
                    "Nos alegra verte nuevamente 🐾",
                    color = textColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            containerColor = MaterialTheme.colorScheme.surface
        )

        LaunchedEffect(Unit) {
            delay(2000)
            showWelcomeDialog = false
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }
}