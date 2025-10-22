package com.example.petdateapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.petdateapp.ui.AgendaScreen
import com.example.petdateapp.ui.GalleryScreen
import com.example.petdateapp.ui.HomeScreen
import com.example.petdateapp.ui.RegisterScreen
import com.example.petdateapp.ui.LoginScreen
import com.example.petdateapp.ui.theme.AppTheme


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class) // Necesario para usar TopAppBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                var expanded by remember { mutableStateOf(false) }

                // Fondo total de la aplicación
                Box(modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)) {

                    // Estado para controlar si el menú desplegable está abierto o cerrado
                var expanded by remember { mutableStateOf(false) }

                // Scaffold = Creamos una estructura base para poder crear un menu inferior y superior
                Scaffold(
                    containerColor = Color.Transparent,
                    // Definimos la barra superior (TopAppBar)
                    topBar = {
                        TopAppBar(
                            title = { Text("PetDate") }, // Título de la aplicación
                            // Acciones (botones) que se alinean a la derecha
                            actions = {
                                // 1. Botón cuadrado de perfil (IconButton)
                                IconButton(onClick = { expanded = true }) {
                                    Icon(
                                        imageVector = Icons.Filled.Person,
                                        contentDescription = "Menú sesión de Usuario"
                                    )
                                }

                                // 2. Menú desplegable (DropdownMenu)
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false } // Se cierra al tocar fuera
                                ) {
                                    // Opción de Inicio de Sesión
                                    DropdownMenuItem(
                                        text = { Text("Inicio de sesión") },
                                        onClick = {
                                            expanded = false // Cerrar el menú

                                             navController.navigate("login")

                                            Log.d("UserAction", "Navegar a Inicio de Sesión")

                                        }
                                    )
                                    // Opción de Registro de Usuario
                                    DropdownMenuItem(
                                        text = { Text("Registro de usuario") },
                                        onClick = {
                                            expanded = false // Cerrar el menú
                                            navController.navigate("registro")

                                            Log.d("UserAction", "Navegar a Registro de Usuario")
                                        }
                                    )
                                }
                            }
                        )
                    },

                    // definimos la barra inferior
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface, // Fondo segun tema
                            contentColor = MaterialTheme.colorScheme.onSurface  // Color base segun tema
                        ) {
                            NavigationBarItem(
                                selected = navController.currentBackStackEntry?.destination?.route == "gallery",
                                onClick = { navController.navigate("gallery") },
                                label = {
                                    Text(
                                        "Galeria",
                                        color = if (navController.currentBackStackEntry?.destination?.route == "gallery")
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Photo,
                                        contentDescription = "Galeria",
                                        tint = if (navController.currentBackStackEntry?.destination?.route == "gallery")
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            )
                            NavigationBarItem(
                                selected = navController.currentBackStackEntry?.destination?.route == "home",
                                onClick = { navController.navigate("home") },
                                label = {
                                    Text(
                                        "Inicio",
                                        color = if (navController.currentBackStackEntry?.destination?.route == "home")
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Home,
                                        contentDescription = "Inicio",
                                        tint = if (navController.currentBackStackEntry?.destination?.route == "home")
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            )
                            NavigationBarItem(
                                selected = navController.currentBackStackEntry?.destination?.route == "agenda",
                                onClick = { navController.navigate("agenda") },
                                label = {
                                    Text(
                                        "Agenda",
                                        color = if (navController.currentBackStackEntry?.destination?.route == "agenda")
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.CalendarToday,
                                        contentDescription = "Agenda",
                                        tint = if (navController.currentBackStackEntry?.destination?.route == "agenda")
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") { HomeScreen() }
                        // Añadiendo rutas dummy para evitar errores si las NavigationBarItem se usan:
                        composable("gallery") { Text("Página de Galería (Aqui se debe colocar la funcion de la pantalla correspondiente)", modifier = Modifier.fillMaxSize()) }
                        composable("calendar") { Text("Página de Calendario (Aqui se debe colocar la funcion de la pantalla correspondiente)", modifier = Modifier.fillMaxSize()) }
                        composable("registro") { RegisterScreen(navController = navController) }
                        composable("gallery") { GalleryScreen() }
                        composable("agenda") { AgendaScreen() }
                        composable("login") { LoginScreen(navController) }
                        // composable("signup") { SignUpScreen() }
                    }
                }
            }
        }
    }
}
}