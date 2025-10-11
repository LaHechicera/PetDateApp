package com.example.petdateapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable //Es un elemento grafico
fun HomeScreen(){
    //Creamos contenedor para apilar elementos sobre elementos
    Box(
        modifier = Modifier.fillMaxSize(), //ocupa toda la pantalla
        contentAlignment = Alignment.Center //Alinia al centro
    ){
        Text("¡Bienvenido a PetDate!", style = MaterialTheme.typography.headlineSmall)
    }
}

//MODIFICAR TODO !!