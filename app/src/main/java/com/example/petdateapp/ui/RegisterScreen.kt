package com.example.petdateapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
            // Mostrar snackbar en una coroutine separada para controlar el tiempo exactamente
            coroutineScope.launch {
                // showSnackbar es suspend, pero lo lanzamos en una coroutine para no bloquear este LaunchedEffect
                snackbarHostState.showSnackbar(
                    message = msg,
                    withDismissAction = false
                )
            }

            // Espera para volver al inicio
            delay(1000)

            // Si fue éxito, navegar y limpiar
            if (msg == "Datos registrados correctamente.") {
                navController.navigate("home") {
                    popUpTo("registro") { inclusive = true }
                }
            }

            // Limpiar mensaje para que no reaparezca al volver
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
                Text("Registro del dueño", style = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ))
                Spacer(modifier = Modifier.height(24.dp)
                )

                // Dueño
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column{
                        OutlinedTextField(
                            value = viewModel.nombreDueno.value,
                            onValueChange = { viewModel.nombreDueno.value = it },
                            label = { Text("Nombre del dueño") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = viewModel.telefono.value,
                            onValueChange = { viewModel.telefono.value = it },
                            label = { Text("Teléfono") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = viewModel.correo.value,
                            onValueChange = { viewModel.correo.value = it },
                            label = { Text("Correo") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = viewModel.contrasena.value,
                            onValueChange = { viewModel.contrasena.value = it },
                            label = { Text("Contraseña") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Mascota
                Text("Registro de la mascota", style = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ))
                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        OutlinedTextField(
                            value = viewModel.nombreMascota.value,
                            onValueChange = { viewModel.nombreMascota.value = it },
                            label = { Text("Nombre de la mascota") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = viewModel.especie.value,
                            onValueChange = { viewModel.especie.value = it },
                            label = { Text("Especie (Ejemplo: Gato)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = viewModel.raza.value,
                            onValueChange = { viewModel.raza.value = it },
                            label = { Text("Raza") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = viewModel.edad.value,
                            onValueChange = { viewModel.edad.value = it },
                            label = { Text("Edad (años)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = viewModel.peso.value,
                            onValueChange = { viewModel.peso.value = it },
                            label = { Text("Peso (kg)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // → Nota: aquí NO navegamos. Solo validamos.
                val context = LocalContext.current

                // Inicializar la BD UNA sola vez (ideal en LaunchedEffect o justo antes del botón)
                LaunchedEffect(Unit) {
                    viewModel.initDB(context)
                }

                Button(
                    onClick = {
                        viewModel.registrar() //valida y guarda
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

            // Snackbar flotante sobre el contenido
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 12.dp)
            ) { snackbarData ->
                // Personalización simple: fondo verde para éxito, rojo para error
                val isSuccess = snackbarData.visuals.message.contains("correctamente")
                Surface(
                    color = if (isSuccess) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
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
