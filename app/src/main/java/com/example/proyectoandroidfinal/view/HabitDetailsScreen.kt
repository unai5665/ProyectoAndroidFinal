package com.example.proyectoandroidfinal.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(navController: NavController, habitId: Int, habitViewModel: HabitViewModel) {
    val habitWithReminders by habitViewModel.habitsWithReminders.observeAsState(emptyList())

    // Obtener el hábito específico
    val habitDetail = habitWithReminders.firstOrNull { it.habit.id == habitId }

    if (habitDetail == null) {
        // Mostrar un mensaje si no se encuentra el hábito
        Text("Hábito no encontrado", modifier = Modifier.fillMaxSize())
        return
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
                    IconButton(onClick = {
                        // Implementar la lógica para editar el hábito
                        navController.navigate("habit_management/${habitDetail.habit.id}")
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar Hábito")
                    }
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
                ReminderItem(reminder = reminder)
            }
        }
    }
}

@Composable
fun ReminderItem(reminder: Reminder) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Hora: ${reminder.reminderTime}")
            Text(text = "Mensaje: ${reminder.message}")
        }
    }
}
