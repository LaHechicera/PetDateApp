package com.example.petdateapp.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.text.format.DateFormat
import android.view.Surface
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petdateapp.viewmodel.AgendaViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    vm: AgendaViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.init(context) // Carga automática de citas del usuario logueado
    }

    var pendingDate by remember { mutableStateOf<LocalDate?>(null) }
    var pendingTime by remember { mutableStateOf<LocalTime?>(null) }
    var showDescDialog by remember { mutableStateOf(false) }
    var desc by remember { mutableStateOf(TextFieldValue("")) }

    fun launchTimePicker(forDate: LocalDate) {
        val is24h = DateFormat.is24HourFormat(context)
        val now = LocalTime.now()
        TimePickerDialog(
            context,
            { _, hour, minute ->
                pendingDate = forDate
                pendingTime = LocalTime.of(hour, minute)
                desc = TextFieldValue("")
                showDescDialog = true
            },
            now.hour,
            now.minute,
            is24h
        ).show()
    }

    fun launchDatePicker() {
        val cal = Calendar.getInstance()
        val dp = DatePickerDialog(
            context,
            { _, y, m, d ->
                launchTimePicker(LocalDate.of(y, m + 1, d))
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        dp.setOnShowListener { dp.datePicker.firstDayOfWeek = Calendar.MONDAY }
        dp.show()
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .height(72.dp)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(16.dp)
                    ),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Agenda",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { launchDatePicker() }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar cita")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(vm.items, key = { it.id }) { item ->
                AgendaItemRow(
                    data = item,
                    onDelete = { vm.removeById(item.id) }
                )
            }
        }

        if (showDescDialog) {
            AlertDialog(
                onDismissRequest = {
                    pendingDate = null
                    pendingTime = null
                    showDescDialog = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val d = pendingDate
                            val t = pendingTime
                            if (d != null && t != null) vm.addAppointment(d, t, desc.text)
                            pendingDate = null
                            pendingTime = null
                            showDescDialog = false
                        }
                    ) { Text("Guardar") }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            pendingDate = null
                            pendingTime = null
                            showDescDialog = false
                        }
                    ) { Text("Cancelar") }
                },
                title = { Text("Nueva cita") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = desc,
                            onValueChange = { desc = it },
                            singleLine = true,
                            label = { Text("Descripción de la cita") },
                            placeholder = { Text("Ej: vacuna antirrábica") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        val is24h = DateFormat.is24HourFormat(context)
                        val dateText = pendingDate?.format(
                            DateTimeFormatter.ofPattern("EEE d MMM yyyy", Locale.getDefault())
                        ) ?: "Sin fecha"
                        val timeText = pendingTime?.let {
                            if (is24h) it.format(DateTimeFormatter.ofPattern("HH:mm"))
                            else it.format(DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault()))
                        } ?: "--:--"

                        Text("Fecha: $dateText")
                        Text("Hora: $timeText")
                    }
                }
            )
        }
    }
}

@Composable
private fun AgendaItemRow(
    data: AgendaViewModel.AgendaItem,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val is24h = DateFormat.is24HourFormat(context)
    val pattern = if (is24h) "EEE d MMM yyyy • HH:mm" else "EEE d MMM yyyy • h:mm a"
    val formatter = remember(pattern) {
        DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    }

    val cardContainerColor = if (isSystemInDarkTheme()) {
        Color(0xFFC2B872)
    } else {
        MaterialTheme.colorScheme.surfaceContainerLow
    }

    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = cardContainerColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(data.dateTime.format(formatter))
                Spacer(Modifier.height(2.dp))
                Text(data.description)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}
