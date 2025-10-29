package com.example.petdateapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.petdateapp.ui.AgendaScreen
import com.example.petdateapp.ui.GalleryScreen
import com.example.petdateapp.ui.HomeScreen
import com.example.petdateapp.ui.RegisterScreen
import com.example.petdateapp.ui.LoginScreen
import com.example.petdateapp.ui.theme.AppTheme
import kotlinx.coroutines.launch

//para la ventana flotante barra derecha
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

import com.example.petdateapp.viewmodel.LogInViewModel


class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppTheme {
                val navController = rememberNavController()
                var expanded by remember { mutableStateOf(false) } //para mostrar/ocultar la barra usuario

                val pagerState = rememberPagerState(
                    initialPage = 1,
                    pageCount = { 3 } // 0=Gallery, 1=Home, 2=Agenda
                )
                val coroutineScope = rememberCoroutineScope()
                var selectedIndex by remember { mutableStateOf(1) }

                LaunchedEffect(pagerState) {
                    snapshotFlow { pagerState.currentPage }.collect { page ->
                        selectedIndex = page
                    }
                }

                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStack?.destination?.route
                val isDarkTheme = isSystemInDarkTheme()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Scaffold(
                        containerColor = Color.Transparent,
                        topBar = {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(WindowInsets.statusBars.asPaddingValues())
                                    .height(64.dp)
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(16.dp),
                                shadowElevation = 0.dp
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val isDarkTheme = isSystemInDarkTheme()

                                    Image(
                                        painter = painterResource(
                                            id = if (isDarkTheme) R.drawable.logo_dark else R.drawable.logo_light
                                        ),
                                        contentDescription = "Logo PetDate",
                                        modifier = Modifier
                                            .height(28.dp)
                                            .padding(end = 8.dp),
                                        contentScale = ContentScale.Fit
                                    )

                                    Text(
                                        text = "PetDate",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.weight(1f)
                                    )

                                    //visibilidad de la barra
                                    IconButton(onClick = { expanded = !expanded }) {
                                        Icon(
                                            imageVector = Icons.Filled.Person,
                                            contentDescription = "Menú sesión de Usuario",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        },
                        bottomBar = {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(16.dp),
                            ) {
                                NavigationBar(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ) {
                                    NavigationBarItem(
                                        selected = (currentRoute == "home" && selectedIndex == 0),
                                        onClick = {
                                            coroutineScope.launch {
                                                if (currentRoute != "home") {
                                                    navController.navigate("home") {
                                                        popUpTo("home") { inclusive = false }
                                                        launchSingleTop = true
                                                    }
                                                }
                                                pagerState.animateScrollToPage(0)
                                            }
                                        },
                                        label = {
                                            Text(
                                                "Galeria",
                                                color = if (currentRoute == "home" && selectedIndex == 0)
                                                    MaterialTheme.colorScheme.primary
                                                else
                                                    MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Filled.Photo,
                                                contentDescription = "Galeria",
                                                tint = if (currentRoute == "home" && selectedIndex == 0)
                                                    MaterialTheme.colorScheme.primary
                                                else
                                                    MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    )

                                    NavigationBarItem(
                                        selected = (currentRoute == "home" && selectedIndex == 1),
                                        onClick = {
                                            coroutineScope.launch {
                                                if (currentRoute != "home") {
                                                    navController.navigate("home") {
                                                        popUpTo("home") { inclusive = false }
                                                        launchSingleTop = true
                                                    }
                                                }
                                                pagerState.animateScrollToPage(1)
                                            }
                                        },
                                        label = {
                                            Text(
                                                "Inicio",
                                                color = if (currentRoute == "home" && selectedIndex == 1)
                                                    MaterialTheme.colorScheme.primary
                                                else
                                                    MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Filled.Home,
                                                contentDescription = "Inicio",
                                                tint = if (currentRoute == "home" && selectedIndex == 1)
                                                    MaterialTheme.colorScheme.primary
                                                else
                                                    MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    )

                                    NavigationBarItem(
                                        selected = (currentRoute == "home" && selectedIndex == 2),
                                        onClick = {
                                            coroutineScope.launch {
                                                if (currentRoute != "home") {
                                                    navController.navigate("home") {
                                                        popUpTo("home") { inclusive = false }
                                                        launchSingleTop = true
                                                    }
                                                }
                                                pagerState.animateScrollToPage(2)
                                            }
                                        },
                                        label = {
                                            Text(
                                                "Agenda",
                                                color = if (currentRoute == "home" && selectedIndex == 2)
                                                    MaterialTheme.colorScheme.primary
                                                else
                                                    MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = Icons.Filled.CalendarToday,
                                                contentDescription = "Agenda",
                                                tint = if (currentRoute == "home" && selectedIndex == 2)
                                                    MaterialTheme.colorScheme.primary
                                                else
                                                    MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            composable("home") {
                                HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier.fillMaxSize()
                                ) { page ->
                                    when (page) {
                                        0 -> GalleryScreen()
                                        1 -> HomeScreen()
                                        2 -> AgendaScreen()
                                    }
                                }
                            }
                            composable("login") {
                                LoginScreen(navController = navController)
                            }
                            composable("registro") {
                                RegisterScreen(navController = navController)
                            }
                        }
                    }

                    // barra costado derecho
                    if (expanded) {
                        Popup(
                            alignment = Alignment.TopEnd, //pegado al borde derecho-superior
                            onDismissRequest = { expanded = false },
                            properties = PopupProperties(
                                focusable = true,                //para cerrar la abrra usuario al tocar fuera
                                dismissOnClickOutside = true,   //cierre automático al tocar fuera
                                dismissOnBackPress = true
                            )
                        ) {

                            Surface(
                                color = if (isDarkTheme) Color(0xB7986416) else Color(0xFFDAD1C6),
                                tonalElevation = 0.dp,
                                shadowElevation = 0.dp,
                                shape = RoundedCornerShape(0.dp),
                                modifier = Modifier
                                    .width(220.dp) // ancho tipo barra compacta
                                    .wrapContentHeight()
                                    .padding(top = 64.dp)
                                    .padding(end = 0.dp) //pegado al borde derecho
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp, horizontal = 10.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Inicio de sesión",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                            .clickable {
                                                expanded = false
                                                navController.navigate("login")
                                                Log.d("UserAction", "Navegar a Inicio de Sesión")
                                            }
                                    )
                                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                                    Text(
                                        text = "Registro de usuario",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                            .clickable {
                                                expanded = false
                                                navController.navigate("registro")
                                                Log.d("UserAction", "Navegar a Registro de Usuario")
                                            }
                                    )

                                    Text(
                                        text = "Cerrar sesión",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.error, // rojo para destacar
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                            .clickable {
                                                expanded = false
                                                // cerramos sesión
                                                val loginViewModel = LogInViewModel()
                                                loginViewModel.initDB(this@MainActivity) // asegurar inicialización
                                                loginViewModel.cerrarSesion()

                                                // Navegar a Login y limpiar historial
                                                navController.navigate("login") {
                                                    popUpTo("home") { inclusive = true } // limpiamos pantalla home
                                                    launchSingleTop = true
                                                }
                                                Log.d("UserAction", "Usuario cerró sesión")
                                            }
                                    )

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
