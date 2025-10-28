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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petdateapp.R

@Composable
fun HomeScreen() {

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
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Texto superior "Bienvenidos"
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(800)) + scaleIn(tween(800))
            ) {
                Text(
                    text = "Bienvenidos",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = MaterialTheme.typography.headlineLarge.fontSize * 1.2f),
                        color = MaterialTheme.colorScheme.onBackground
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

            // Texto inferior "a PetDate!"
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(900)) + scaleIn(tween(900))
            ) {
                Text(
                    text = "a PetDate!",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
