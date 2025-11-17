package com.example.petdateapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petdateapp.R
import com.example.petdateapp.viewmodel.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    val ownerName by viewModel.ownerName.collectAsState()

    var start by remember { mutableStateOf(false) }

    // Animación base de escala
    val scale by animateFloatAsState(
        targetValue = if (start) 1f else 0.5f,
        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
        label = "scale"
    )

    // Animación adicional de visibilidad
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        start = true
        visible = true
    }

    val isDarkTheme = isSystemInDarkTheme() // Detectar tema oscuro o claro
    val logoRes = if (isDarkTheme) com.example.petdateapp.R.drawable.logo_dark else R.drawable.logo_light // Seleccionar logo según tema

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .scale(scale),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 32.dp)
        ) {

            // Texto superior "Bienvenidos"
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(800)) + scaleIn(tween(800))
            ) {
                val welcomeMessage = "Bienvenido a PetDate" + if (ownerName.isNotEmpty()) {
                    " ${ownerName.replaceFirstChar { it.uppercase() }}!"
                } else {
                    "!"
                }
                Text(
                    text = welcomeMessage,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize * 1.2f
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth(), //
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(24.dp))


            // Imagen del Logo
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(1000)) + scaleIn(
                    animationSpec = tween(1000, easing = LinearOutSlowInEasing)
                )
            ) {
                Image(
                    painter = painterResource(id = logoRes),
                    contentDescription = "Logo PetDate",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

        }
    }
}
