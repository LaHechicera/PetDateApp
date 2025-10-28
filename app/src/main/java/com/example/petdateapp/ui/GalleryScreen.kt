package com.example.petdateapp.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage // Se usa la oficial de Coil
import com.example.petdateapp.viewmodel.GalleryViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel = viewModel()
) {
    // Launcher del Photo Picker (permite selección múltiple; el VM recorta a 6)
    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(6)
    ) { uris: List<Uri> ->
        viewModel.addImages(uris)
    }

    val images = viewModel.images // List<Uri>

    Scaffold(
        topBar = {

            //Barra personalizada con borde y texto centrado
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .height(72.dp)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .border(
                        width = 1.dp, //Aumentar grosor del bordedp
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(16.dp)
                    ),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center //Centramos el texto visualmente
                ) {
                    Text(
                        text = "Galería",
                        style = MaterialTheme.typography.headlineSmall, // Estilo original solicitado
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    )  { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Acción principal: Agregar imágenes (deshabilitado si no hay cupo)
            Button(
                onClick = {
                    picker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                enabled = viewModel.canAddMore()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(
                    if (viewModel.canAddMore())
                        "Agregar (${viewModel.remainingSlots()} cupo(s))"
                    else
                        "Límite alcanzado"
                )
            }

            Spacer(Modifier.height(12.dp))

            // Grid 2 columnas. Mostramos exactamente 6 slots visuales sin recordar estado extra.
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(6) { index ->
                    // getOrNull para evitar errores de índice
                    val uri = images.getOrNull(index)

                    if (uri != null) {
                        ImageCard(
                            uri = uri,
                            onRemove = { viewModel.removeAt(index) }
                        )
                    } else {
                        PlaceholderCard(
                            enabled = viewModel.canAddMore(),
                            onClick = {
                                if (viewModel.canAddMore()) {
                                    picker.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
//Tarjeta para una imagen con botón de quitar.
private fun ImageCard(
    uri: Uri,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f) //mantiene forma cuadrada
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
    ) {

        AsyncImage(
            model = uri,
            contentDescription = "Imagen seleccionada",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Botón de cerrar arriba a la derecha
        FilledTonalIconButton(
            onClick = onRemove,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp),
            colors = IconButtonDefaults.filledTonalIconButtonColors()
        ) {
            Icon(Icons.Default.Close, contentDescription = "Quitar")
        }
    }
}


@Composable
//Tarjeta placeholder con ícono de "agregar"
private fun PlaceholderCard(
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f) // cuadrado
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            .let { base ->
                if (enabled) base.clickable(onClick = onClick) else base
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.height(4.dp))
            Text(
                if (enabled) "Agregar" else "Sin cupo",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
