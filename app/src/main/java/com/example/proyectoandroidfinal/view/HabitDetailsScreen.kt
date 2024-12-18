package com.example.proyectoandroidfinal.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.livedata.observeAsState
import com.example.proyectoandroidfinal.model.Reminder
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectoandroidfinal.model.HabitWithReminders
import com.example.proyectoandroidfinal.viewmodel.HabitViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(navController: NavController, habitId: Int, habitViewModel: HabitViewModel) {
    val habitWithReminders by habitViewModel.habitsWithReminders.observeAsState(emptyList())

    // Verificar si no se encuentran datos
    val habitDetail = habitWithReminders.firstOrNull { it.habit.id == habitId }

    if (habitDetail == null) {
        // Mostrar un mensaje si no se encuentra el hábito
        Text("Hábito no encontrado", modifier = Modifier.fillMaxSize())
        return
    }

    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var reminderToDelete by remember { mutableStateOf<Reminder?>(null) }

    if (showDeleteConfirmation && reminderToDelete != null) {
        // Mostrar el diálogo de confirmación
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar este recordatorio?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Eliminar el recordatorio
                        reminderToDelete?.let { habitViewModel.deleteReminder(it.id) }
                        showDeleteConfirmation = false
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(habitDetail.habit.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Nombre: ${habitDetail.habit.name}", fontWeight = FontWeight.Bold)
                    Text(text = "Categoría: ${habitDetail.habit.category}")
                    Text(text = "Frecuencia: ${habitDetail.habit.frequency}")
                    Text(text = "Creado en: ${habitDetail.habit.createdAt}")
                }
            }
            item {
                Text(
                    text = "Recordatorios",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            items(habitDetail.reminders) { reminder ->
                ReminderItem(
                    reminder = reminder,  // Asegúrate de pasar el objeto completo reminder
                    onDelete = { reminder ->
                        reminderToDelete = reminder
                        showDeleteConfirmation = true
                    }
                )
            }
        }
    }
}
@Composable
fun ReminderItem(reminder: Reminder, onDelete: (Reminder) -> Unit) {
    // Verificar si reminderTime es válido antes de formatearlo
    val formattedTime = if (reminder.reminderTime > 0) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(reminder.reminderTime))
    } else {
        "Hora inválida" // Valor por defecto si el timestamp es incorrecto
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Información del recordatorio
            Column {
                Text(text = "Hora: $formattedTime") // Mostrar la hora formateada
                Text(text = "Mensaje: ${reminder.message}") // Mostrar el mensaje
            }
            // Botón para borrar
            IconButton(onClick = { onDelete(reminder) }) {  // Pasamos el objeto completo reminder
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar Recordatorio",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
