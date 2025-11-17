package com.example.petdateapp.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.petdateapp.R
import com.example.petdateapp.viewmodel.HomeViewModel

// Modelo de datos local temporal hasta que el microservicio esté listo
data class Pet(
    val id: Long = System.currentTimeMillis(), // ID único temporal
    var name: String,
    var imageUrl: String?,
    var animalType: String,
    var weight: String,
    var allergies: String,
    var food: String,
    var bio: String
)

@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    val ownerName by viewModel.ownerName.collectAsState()

    // --- STATE MANAGEMENT ---
    // Lista local de mascotas. Se llenará desde el microservicio en el futuro.
    val pets = remember { mutableStateListOf<Pet>() }
    // Controla la visibilidad del diálogo para añadir/editar mascota
    var showAddPetDialog by remember { mutableStateOf(false) }
    // Guarda la mascota que se está editando. Si es null, el diálogo es para añadir una nueva.
    var petToEdit by remember { mutableStateOf<Pet?>(null) }
    // Controla la visibilidad del diálogo de opciones (Modificar/Eliminar)
    var showOptionsDialog by remember { mutableStateOf(false) }
    // Mascota seleccionada para mostrar opciones
    var selectedPet by remember { mutableStateOf<Pet?>(null) }


    // --- DIALOGS ---
    // Diálogo para añadir o editar una mascota
    if (showAddPetDialog) {
        AddEditPetDialog(
            pet = petToEdit,
            onDismiss = { showAddPetDialog = false },
            onSave = { petData ->
                // TODO: Aquí se llamará al ViewModel para guardar en el microservicio
                if (petToEdit == null) {
                    // Añadir nueva mascota a la lista local
                    pets.add(petData)
                } else {
                    // Actualizar mascota existente en la lista local
                    val index = pets.indexOfFirst { it.id == petData.id }
                    if (index != -1) {
                        pets[index] = petData
                    }
                }
                showAddPetDialog = false
            }
        )
    }

    // Diálogo para mostrar opciones de modificar o eliminar
    if (showOptionsDialog && selectedPet != null) {
        OptionsDialog(
            pet = selectedPet!!,
            onDismiss = { showOptionsDialog = false },
            onEdit = {
                petToEdit = it
                showOptionsDialog = false
                showAddPetDialog = true
            },
            onDelete = { petToDelete ->
                // TODO: Aquí se llamará al ViewModel para eliminar en el microservicio
                pets.remove(petToDelete)
                showOptionsDialog = false
            }
        )
    }


    // --- UI ---
    val isDarkTheme = isSystemInDarkTheme()
    val logoRes = if (isDarkTheme) R.drawable.logo_dark else R.drawable.logo_light

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- MENSAJE DE BIENVENIDA ---
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

        // --- LOGO ---
        Image(
            painter = painterResource(id = logoRes),
            contentDescription = "Logo PetDate",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECCIÓN DE MASCOTAS ---
        Text(
            "Tus Mascotas",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Fila horizontal para mostrar las mascotas y el botón de añadir
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Botón para añadir nueva mascota
            item {
                AddPetButton {
                    petToEdit = null // Aseguramos que el diálogo sea para añadir
                    showAddPetDialog = true
                }
            }

            // Lista de mascotas existentes
            items(pets) { pet ->
                PetCard(pet = pet) {
                    selectedPet = pet
                    showOptionsDialog = true
                }
            }
        }
    }
}


@Composable
fun AddPetButton(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Añadir Mascota",
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun PetCard(pet: Pet, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Si hay URL de imagen, la muestra. Si no, muestra un ícono.
            if (pet.imageUrl != null) {
                AsyncImage(
                    model = pet.imageUrl,
                    contentDescription = pet.name,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.logo_dark) // Un placeholder mientras carga
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = "Icono de Mascota",
                    modifier = Modifier.size(60.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(pet.name, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun AddEditPetDialog(
    pet: Pet?, // Si es null, es un alta. Si no, es una modificación.
    onDismiss: () -> Unit,
    onSave: (Pet) -> Unit
) {
    val isEditMode = pet != null
    val context = LocalContext.current

    // Estado para cada campo del formulario
    var name by remember { mutableStateOf(pet?.name ?: "") }
    var imageUrl by remember { mutableStateOf(pet?.imageUrl) }
    var animalType by remember { mutableStateOf(pet?.animalType ?: "") }
    var weight by remember { mutableStateOf(pet?.weight ?: "") }
    var allergies by remember { mutableStateOf(pet?.allergies ?: "") }
    var food by remember { mutableStateOf(pet?.food ?: "") }
    var bio by remember { mutableStateOf(pet?.bio ?: "") }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        // TODO: En un caso real, subirías esta imagen a un servicio (ej. S3, Firebase Storage)
        // y guardarías la URL. Por ahora, solo guardamos la URI local.
        imageUrl = uri?.toString()
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

                // Campos del formulario
                Button(onClick = { imagePicker.launch("image/*") }) {
                    Text("Seleccionar Imagen")
                }
                if (imageUrl != null) {
                    AsyncImage(model = imageUrl, contentDescription = "Imagen seleccionada", modifier = Modifier.size(100.dp))
                }

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                OutlinedTextField(value = animalType, onValueChange = { animalType = it }, label = { Text("Tipo de Animal") })
                OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Peso (ej. 5.2 kg)") })
                OutlinedTextField(value = allergies, onValueChange = { allergies = it }, label = { Text("Alergias") })
                OutlinedTextField(value = food, onValueChange = { food = it }, label = { Text("Alimento") })
                OutlinedTextField(value = bio, onValueChange = { bio = it }, label = { Text("Biografía / Observaciones") }, maxLines = 4)

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        val petData = pet?.copy(
                            name = name,
                            imageUrl = imageUrl,
                            animalType = animalType,
                            weight = weight,
                            allergies = allergies,
                            food = food,
                            bio = bio
                        ) ?: Pet(
                            name = name,
                            imageUrl = imageUrl,
                            animalType = animalType,
                            weight = weight,
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
fun OptionsDialog(
    pet: Pet,
    onDismiss: () -> Unit,
    onEdit: (Pet) -> Unit,
    onDelete: (Pet) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Opciones para ${pet.name}", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onEdit(pet) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Modificar")
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { onDelete(pet) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar")
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}