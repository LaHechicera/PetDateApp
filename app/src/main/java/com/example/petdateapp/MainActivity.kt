package com.example.petdateapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.petdateapp.ui.HomeScreen
import com.example.petdateapp.ui.theme.PetDateAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetDateAPPTheme {
                //Memorizador para saber en que pagina estoy
                val navController = rememberNavController()

                //Scaffold = Creamos una estructura base para poder crear un menu inferior
                Scaffold(
                    //definimos la barra inferior
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                //navControler = Objeto que controla la navegacion
                                //currentBackStackEntry = Representa la ultima pantalla que se visualizo
                                //destination = hacia donde estamos apuntando nuestra direccion
                                //route = indica la ruta, que es igual a inicio en este caso
                                selected = navController.currentBackStackEntry?.destination?.route == "home",
                                onClick = { navController.navigate("home") },
                                label = { Text("Galeria") },
                                icon = { Icon(Icons.Filled.Photo, "Inicio") } //Aqui va Galeria
                            )
                            NavigationBarItem(
                                //navControler = Objeto que controla la navegacion
                                //currentBackStackEntry = Representa la ultima pantalla que se visualizo
                                //destination = hacia donde estamos apuntando nuestra direccion
                                //route = indica la ruta, que es igual a inicio en este caso
                                selected = navController.currentBackStackEntry?.destination?.route == "home",
                                onClick = { navController.navigate("home") },
                                label = { Text("Inicio") },
                                icon = { Icon(Icons.Filled.Home, "Inicio") }
                            )
                            NavigationBarItem(
                                //navControler = Objeto que controla la navegacion
                                //currentBackStackEntry = Representa la ultima pantalla que se visualizo
                                //destination = hacia donde estamos apuntando nuestra direccion
                                //route = indica la ruta, que es igual a inicio en este caso
                                selected = navController.currentBackStackEntry?.destination?.route == "home",
                                onClick = { navController.navigate("home") },
                                label = { Text("Calendario") },
                                icon = { Icon(Icons.Filled.CalendarToday, "Inicio") } //Aqui va Fechas
                            )
                        }
                    }
                ) {

                        innerPadding ->
                    NavHost(
                        navController = navController, //Usamos el mismo controlador que especificamos arriba
                        startDestination = "home",  //Que inicie en el home
                        modifier = Modifier.padding(innerPadding) //Aplica espaciado para que las barras no se superpongan
                    ) {
                        composable("home") { HomeScreen() }
                    }
                }
            }
        }
    }
}