package com.example.petdateapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.petdateapp.data.PetRepository
import com.example.petdateapp.data.datastore.ProfileDataStore
import com.example.petdateapp.ui.AgendaScreen
import com.example.petdateapp.ui.GalleryScreen
import com.example.petdateapp.ui.HomeScreen
import com.example.petdateapp.ui.LoginScreen
import com.example.petdateapp.ui.ProfileScreen
import com.example.petdateapp.ui.RegisterScreen
import com.example.petdateapp.ui.theme.AppTheme
import com.example.petdateapp.data.datastore.UserDataStore
import com.example.petdateapp.viewmodel.HomeViewModel
import com.example.petdateapp.viewmodel.HomeViewModelFactory
import com.example.petdateapp.viewmodel.LogInViewModel
import com.example.petdateapp.viewmodel.ProfileViewModel
import com.example.petdateapp.viewmodel.ProfileViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//para la ventana flotante barra derecha
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppTheme {
                val context = LocalContext.current
                val userDataStore = UserDataStore(context)
                val profileDataStore = ProfileDataStore(context)
                val petRepository = PetRepository()
                val initialRoute by produceState<String?>(initialValue = null) {
                    val userEmail = userDataStore.userEmailFlow.first()
                    value = if (userEmail.isNullOrBlank()) "login" else "home"
                }

                if (initialRoute != null) {
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
                                // ✅ Solo mostramos la top bar cuando estamos en "home"
                                if (currentRoute == "home") {
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
                                            val isDarkThemeTop = isSystemInDarkTheme()

                                            Image(
                                                painter = painterResource(
                                                    id = if (isDarkThemeTop) R.drawable.logo_light else R.drawable.logo_light
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
                                }
                            },
                            bottomBar = {
                                // ✅ Solo mostramos la barra de navegación cuando estamos en "home"
                                if (currentRoute == "home") {
                                    Surface(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(16.dp),
                                    ) {
                                        NavigationBar(
                                            containerColor = Color.Transparent,
                                            contentColor = MaterialTheme.colorScheme.onSurface
                                        ) {
                                            val selectedColor = Color(0xFF64504A)
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
                                                        pagerState.animateScrollToPage(
                                                            page = 0,
                                                            animationSpec = tween(durationMillis = 500)
                                                        )
                                                    }
                                                },
                                                label = {
                                                    Text(
                                                        "Galeria",
                                                        color = if (currentRoute == "home" && selectedIndex == 0)
                                                            selectedColor
                                                        else
                                                            MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                },
                                                icon = {
                                                    Icon(
                                                        imageVector = Icons.Filled.Photo,
                                                        contentDescription = "Galeria",
                                                        tint = if (currentRoute == "home" && selectedIndex == 0)
                                                            selectedColor
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
                                                        pagerState.animateScrollToPage(
                                                            page = 1,
                                                            animationSpec = tween(durationMillis = 500)
                                                        )
                                                    }
                                                },
                                                label = {
                                                    Text(
                                                        "Inicio",
                                                        color = if (currentRoute == "home" && selectedIndex == 1)
                                                            selectedColor
                                                        else
                                                            MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                },
                                                icon = {
                                                    Icon(
                                                        imageVector = Icons.Filled.Home,
                                                        contentDescription = "Inicio",
                                                        tint = if (currentRoute == "home" && selectedIndex == 1)
                                                            selectedColor
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
                                                        pagerState.animateScrollToPage(
                                                            page = 2,
                                                            animationSpec = tween(durationMillis = 500)
                                                        )
                                                    }
                                                },
                                                label = {
                                                    Text(
                                                        "Agenda",
                                                        color = if (currentRoute == "home" && selectedIndex == 2)
                                                            selectedColor
                                                        else
                                                            MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                },
                                                icon = {
                                                    Icon(
                                                        imageVector = Icons.Filled.CalendarToday,
                                                        contentDescription = "Agenda",
                                                        tint = if (currentRoute == "home" && selectedIndex == 2)
                                                            selectedColor
                                                        else
                                                            MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        ) { innerPadding ->
                            NavHost(
                                navController = navController,
                                startDestination = initialRoute!!, // 👈 Inicia siempre en la pantalla de inicio de sesión
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {
                                composable("home") {
                                    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(userDataStore, petRepository))
                                    HorizontalPager(
                                        state = pagerState,
                                        modifier = Modifier.fillMaxSize()
                                    ) { page ->
                                        when (page) {
                                            0 -> GalleryScreen()
                                            1 -> HomeScreen(homeViewModel)
                                            2 -> AgendaScreen()
                                        }
                                    }
                                }
                                composable("login") {
                                    val logInViewModel: LogInViewModel = viewModel()
                                    LoginScreen(
                                        navController = navController,
                                        logInViewModel = logInViewModel
                                    )
                                }
                                composable("registro") {
                                    RegisterScreen(navController = navController)
                                }
                                composable("profile") {
                                    val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(userDataStore, profileDataStore))
                                    ProfileScreen(profileViewModel)
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
                                    color = MaterialTheme.colorScheme.surfaceVariant,
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
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp)
                                                .clickable {
                                                    expanded = false
                                                    navController.navigate("profile")
                                                    Log.d("UserAction", "Navegando a Perfil")
                                                }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.AccountCircle,
                                                contentDescription = "Icono de perfil",
                                                tint = Color(0xFF120B06)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Mi Perfil",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = Color(0xFF120B06)
                                            )
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
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
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.ExitToApp,
                                                contentDescription = "Icono de cerrar sesión",
                                                tint = Color(0xFF120B06)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Cerrar sesión",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = Color(0xFF120B06)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Muestra un indicador de carga mientras se verifica el estado de la sesión
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
