package com.example.petdateapp.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.petdateapp.R
import com.example.petdateapp.network.dto.PetDto
import com.example.petdateapp.utils.ImageUtils
import com.example.petdateapp.viewmodel.HomeViewModel
import com.example.petdateapp.viewmodel.PetsUiState

@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    val ownerName by viewModel.ownerName.collectAsState()
    // Observamos el estado de la UI de las mascotas desde el ViewModel
    val petsUiState by viewModel.petsUiState.collectAsState()

    // --- STATE MANAGEMENT for Dialogs ---
    var showAddPetDialog by remember { mutableStateOf(false) }
    var petToEdit by remember { mutableStateOf<PetDto?>(null) }
    var showPetDetailsDialog by remember { mutableStateOf(false) }
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }
    var selectedPet by remember { mutableStateOf<PetDto?>(null) }


    // --- DIALOGS ---
    if (showAddPetDialog) {
        AddEditPetDialog(
            pet = petToEdit,
            onDismiss = { showAddPetDialog = false },
            onSave = {
                if (petToEdit == null) {
                    // El ownerId se asigna ahora en el ViewModel
                    viewModel.createPet(it)
                } else {
                    viewModel.updatePet(it.id!!, it)
                }
                showAddPetDialog = false
            }
        )
    }

    if (showPetDetailsDialog && selectedPet != null) {
        PetDetailsDialog(
            pet = selectedPet!!,
            onDismiss = { showPetDetailsDialog = false },
            onEdit = {
                petToEdit = it
                showPetDetailsDialog = false
                showAddPetDialog = true
            },
            onDelete = { showConfirmDeleteDialog = true }
        )
    }

    if (showConfirmDeleteDialog && selectedPet != null) {
        ConfirmDeleteDialog(
            petName = selectedPet!!.name,
            onDismiss = { showConfirmDeleteDialog = false },
            onConfirm = {
                viewModel.deletePet(selectedPet!!.id!!)
                showConfirmDeleteDialog = false
                showPetDetailsDialog = false
            }
        )
    }


    // --- UI ---    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val welcomeMessage = "Bienvenido a PetDate" + if (ownerName.isNotEmpty()) {
            " ${ownerName.replaceFirstChar { it.uppercase() }}!"
        } else {
            "!"
        }
        Text(
            text = welcomeMessage,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 16.dp, end = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))
        
        val isDarkTheme = isSystemInDarkTheme()
        val logoRes = if (isDarkTheme) R.drawable.logo_dark else R.drawable.logo_light
        Image(
            painter = painterResource(id = logoRes),
            contentDescription = "Logo PetDate",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Tus Mascotas",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- Contenido dinámico basado en el estado de la UI ---
        when (val state = petsUiState) {
            is PetsUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
            is PetsUiState.Success -> {
                PetList(pets = state.pets, onAddPet = {
                    petToEdit = null
                    showAddPetDialog = true
                }, onPetClick = {                    selectedPet = it
                    showPetDetailsDialog = true
                })
            }
            is PetsUiState.Error -> {
                Text(
                    text = "Error al cargar las mascotas. Inténtalo de nuevo.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun PetList(
    pets: List<PetDto>,
    onAddPet: () -> Unit,
    onPetClick: (PetDto) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { AddPetButton(onClick = onAddPet) }
        items(pets) { pet ->
            PetCard(pet = pet) { onPetClick(pet) }
        }
    }
}


@Composable
fun AddPetButton(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(120.dp)
            .clickable(onClick = onClick),
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Añadir Mascota",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun PetCard(pet: PetDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(120.dp)
            .clickable(onClick = onClick),
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (!pet.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = pet.imageUrl, // Ahora la URL es una Uri del almacenamiento interno
                    contentDescription = pet.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.logo_dark),
                    error = painterResource(id = R.drawable.logo_dark)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = "Icono de Mascota",
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    }
}

@Composable
fun AddEditPetDialog(
    pet: PetDto?,
    onDismiss: () -> Unit,
    onSave: (PetDto) -> Unit
) {
    val isEditMode = pet != null
    val context = LocalContext.current

    var name by remember { mutableStateOf(pet?.name ?: "") }
    // Ahora imageUrl es la URI del archivo en formato String
    var imageUrl by remember { mutableStateOf(pet?.imageUrl) }
    var animalType by remember { mutableStateOf(pet?.animalType ?: "") }
    var weight by remember { mutableStateOf(pet?.weight?.toString() ?: "") }
    var allergies by remember { mutableStateOf(pet?.allergies ?: "") }
    var food by remember { mutableStateOf(pet?.food ?: "") }
    var bio by remember { mutableStateOf(pet?.bio ?: "") }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Guardamos la imagen en el almacenamiento interno y obtenemos su nueva Uri
            val newImageUri = ImageUtils.saveImageToInternalStorage(context, it)
            imageUrl = newImageUri?.toString()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (isEditMode) "Modificar Mascota" else "Añadir Mascota",
                    style = MaterialTheme.typography.headlineSmall
                )

                Button(onClick = { imagePicker.launch("image/*") }) {
                    Text("Seleccionar Imagen")
                }
                if (imageUrl != null) {
                    AsyncImage(model = imageUrl, contentDescription = "Imagen seleccionada", modifier = Modifier.size(100.dp))
                }

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                OutlinedTextField(value = animalType, onValueChange = { animalType = it }, label = { Text("Tipo de Animal") })
                OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Peso (ej. 5.2)") })
                OutlinedTextField(value = allergies, onValueChange = { allergies = it }, label = { Text("Alergias") })
                OutlinedTextField(value = food, onValueChange = { food = it }, label = { Text("Alimento") })
                OutlinedTextField(value = bio, onValueChange = { bio = it }, label = { Text("Biografía / Observaciones") }, maxLines = 4)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {                        val petData = pet?.copy(
                            name = name,
                            imageUrl = imageUrl,
                            animalType = animalType,
                            weight = weight.toDoubleOrNull() ?: 0.0,
                            allergies = allergies,
                            food = food,
                            bio = bio
                        ) ?: PetDto(
                            id = null, // ID es null para la creación
                            ownerId = "", // El ViewModel lo completará
                            name = name,
                            imageUrl = imageUrl,
                            animalType = animalType,
                            weight = weight.toDoubleOrNull() ?: 0.0,
                            allergies = allergies,
                            food = food,
                            bio = bio
                        )
                        onSave(petData)
                    }) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}

@Composable
fun PetDetailsDialog(
    pet: PetDto,
    onDismiss: () -> Unit,
    onEdit: (PetDto) -> Unit,
    onDelete: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(pet.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))

                if (!pet.imageUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = pet.imageUrl, // La URL es ahora una URI del almacenamiento interno
                        contentDescription = pet.name,
                        modifier = Modifier.size(150.dp).clip(CircleShape)
                    )
                } else {
                    Icon(imageVector = Icons.Default.Pets, contentDescription = "Icono mascota", modifier = Modifier.size(100.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Detalles de la mascota
                Text("Tipo: ${pet.animalType}")
                Text("Peso: ${pet.weight} kg")
                Text("Alergias: ${pet.allergies}")
                Text("Alimento: ${pet.food}")
                Text("Bio: ${pet.bio}")
                
                Spacer(modifier = Modifier.height(24.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(onClick = { onEdit(pet) }) {
                        Text("Modificar")
                    }
                    Button(onClick = onDelete, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmDeleteDialog(
    petName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("¿Estás seguro?") },
        text = { Text("Se eliminará a '$petName' de forma permanente.") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Sí, eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}

