package com.example.petdateapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.petdateapp.viewmodel.RegisterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(viewModel: RegisterViewModel = viewModel(), navController: NavController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Observa cambios en el mensaje y muestra Snackbar + navega (si es éxito) después de 2s
    LaunchedEffect(viewModel.mensaje.value) {
        val msg = viewModel.mensaje.value
        if (msg.isNotEmpty()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = msg,
                    withDismissAction = false
                )
            }

            delay(1000)

            if (msg == "Datos registrados correctamente.") {
                navController.navigate("home") {
                    popUpTo("registro") { inclusive = true }
                }
            }

            viewModel.mensaje.value = ""
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background //evita fondo negro
    ) {
        Box {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Registro del dueño",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Dueño
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(16.dp)
                ) {
                    Column {
                        OutlinedTextField(
                            value = viewModel.correo.value,
                            onValueChange = { viewModel.correo.value = it },
                            label = { Text("Nombre del dueño") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = viewModel.telefono.value,
                            onValueChange = { viewModel.telefono.value = it },
                            label = { Text("Teléfono") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Transparent)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = viewModel.correo.value,
                            onValueChange = { viewModel.correo.value = it },
                            label = { Text("Correo") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Transparent)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = viewModel.contrasena.value,
                            onValueChange = { viewModel.contrasena.value = it },
                            label = { Text("Contraseña") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Transparent)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Mascota
                Text(
                    "Registro de la mascota",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Transparent)
                        .padding(16.dp)
                ) {
                    Column {
                        OutlinedTextField(
                            value = viewModel.nombreMascota.value,
                            onValueChange = { viewModel.nombreMascota.value = it },
                            label = { Text("Nombre de la mascota") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Transparent)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = viewModel.especie.value,
                            onValueChange = { viewModel.especie.value = it },
                            label = { Text("Especie (Ejemplo: Gato)") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Transparent)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = viewModel.raza.value,
                            onValueChange = { viewModel.raza.value = it },
                            label = { Text("Raza") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Transparent)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = viewModel.edad.value,
                            onValueChange = { viewModel.edad.value = it },
                            label = { Text("Edad (años)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Transparent)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = viewModel.peso.value,
                            onValueChange = { viewModel.peso.value = it },
                            label = { Text("Peso (kg)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Transparent)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                val context = LocalContext.current

                LaunchedEffect(Unit) {
                    viewModel.initDB(context)
                }

                Button(
                    onClick = {
                        viewModel.registrar()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Registrar Mascota")
                }
            }

            // Snackbar flotante
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp)
            ) { snackbarData ->
                val isSuccess = snackbarData.visuals.message.contains("correctamente")
                Surface(
                    color = if (isSuccess) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = snackbarData.visuals.message,
                        color = Color.White,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() { }
