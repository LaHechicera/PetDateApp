package com.example.petdateapp.ui

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

@Composable //Es un elemento grafico
fun HomeScreen(){

    var start by remember { mutableStateOf(false) }

    // Animación sencilla de escala
    val scale by animateFloatAsState(
        targetValue = if (start) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
        label = "scale"
    )

    // Inicia la animación al abrir la pantalla
    LaunchedEffect(Unit) { start = true }

    //Creamos contenedor para apilar elementos sobre elementos
    Box(
        modifier = Modifier.fillMaxSize().scale(scale), //ocupa toda la pantalla
        contentAlignment = Alignment.Center //Alinia al centro
    ){
        Text("¡Bienvenido a PetDate!", style = MaterialTheme.typography.headlineSmall)
    }
}
