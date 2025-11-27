package com.example.petdateapp.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.petdateapp.R
import com.example.petdateapp.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {
    val profileState by profileViewModel.profileState.collectAsState()
    var isEditing by remember { mutableStateOf(false) }

    var nombre by remember { mutableStateOf(profileState.nombre) }
    var telefono by remember { mutableStateOf(profileState.telefono) }
    var genero by remember { mutableStateOf(profileState.genero) }
    var genderMenuExpanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Masculino", "Femenino", "Otro")

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { profileViewModel.updateProfileImage(it) }
    }

    LaunchedEffect(profileState) {
        nombre = profileState.nombre
        telefono = profileState.telefono
        genero = profileState.genero
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .background(Color.Gray)
            .clickable(enabled = isEditing) {
                imagePickerLauncher.launch("image/*")
            }
        ) {
            AsyncImage(
                model = profileState.imagenUri ?: R.drawable.ic_launcher_foreground,
                contentDescription = "Profile Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_launcher_foreground)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            enabled = isEditing,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = profileState.email,
            onValueChange = {},
            label = { Text("Correo Electrónico") },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            enabled = isEditing,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = genderMenuExpanded,
            onExpandedChange = { genderMenuExpanded = it }, 
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = genero,
                onValueChange = {},
                readOnly = true,
                label = { Text("Género") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderMenuExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                enabled = isEditing
            )
            ExposedDropdownMenu(
                expanded = genderMenuExpanded && isEditing,
                onDismissRequest = { genderMenuExpanded = false }
            ) {
                genderOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            genero = option
                            genderMenuExpanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (isEditing) {
                    profileViewModel.updateProfile(nombre, telefono, genero)
                }
                isEditing = !isEditing
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isEditing) "Guardar Cambios" else "Modificar Perfil")
        }
    }
}