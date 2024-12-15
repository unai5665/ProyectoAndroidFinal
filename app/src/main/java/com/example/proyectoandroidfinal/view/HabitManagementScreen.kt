package com.example.proyectoandroidfinal.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.proyectoandroidfinal.model.Habit
import com.example.proyectoandroidfinal.viewmodel.HabitViewModel

@Composable
fun HabitManagementScreen(habitViewModel: HabitViewModel = viewModel(), navController: NavController) {
    // Estados para los datos
    val habits by habitViewModel.habits.observeAsState(emptyList())
    var habitName by remember { mutableStateOf("") }
    var habitCategory by remember { mutableStateOf("") }
    var habitFrequency by remember { mutableStateOf("") }
    var habitReminderTime by remember { mutableStateOf("") }
    var selectedHabit by remember { mutableStateOf<Habit?>(null) } // Para editar
    var errorMessage by remember { mutableStateOf("") }

    // UI
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Gestión de Hábitos", style = MaterialTheme.typography.titleLarge)

        // Formulario
        OutlinedTextField(
            value = habitName,
            onValueChange = { habitName = it },
            label = { Text("Nombre del Hábito") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = habitCategory,
            onValueChange = { habitCategory = it },
            label = { Text("Categoría") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = habitFrequency,
            onValueChange = { habitFrequency = it },
            label = { Text("Frecuencia") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = habitReminderTime,
            onValueChange = { habitReminderTime = it },
            label = { Text("Hora del Recordatorio") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Mostrar mensaje de error si los campos están vacíos
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        // Botón para agregar o actualizar hábito
        Button(onClick = {
            if (habitName.isEmpty() || habitCategory.isEmpty() || habitFrequency.isEmpty() || habitReminderTime.isEmpty()) {
                errorMessage = "Todos los campos son obligatorios."
            } else {
                if (selectedHabit == null) {
                    // Agregar nuevo hábito
                    habitViewModel.insertHabit(
                        Habit(
                            name = habitName,
                            category = habitCategory,
                            frequency = habitFrequency,
                            reminderTime = habitReminderTime
                        )
                    )
                } else {
                    // Editar hábito existente
                    val updatedHabit = selectedHabit!!.copy(
                        name = habitName,
                        category = habitCategory,
                        frequency = habitFrequency,
                        reminderTime = habitReminderTime
                    )
                    habitViewModel.updateHabit(updatedHabit)
                }
                // Limpiar formulario
                habitName = ""
                habitCategory = ""
                habitFrequency = ""
                habitReminderTime = ""
                selectedHabit = null
                errorMessage = ""
            }
        }) {
            Text(if (selectedHabit == null) "Agregar Hábito" else "Actualizar Hábito")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Lista de hábitos existentes
        Text(text = "Tus Hábitos", style = MaterialTheme.typography.titleMedium)
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(habits) { habit ->
                HabitListItem(habit = habit,
                    onEdit = {
                        selectedHabit = habit
                        habitName = habit.name
                        habitCategory = habit.category
                        habitFrequency = habit.frequency
                        habitReminderTime = habit.reminderTime
                    },
                    onDelete = {
                        habitViewModel.deleteHabit(habit)
                    }
                )
            }
        }
    }
}

@Composable
fun HabitListItem(habit: Habit, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = habit.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "Categoría: ${habit.category}")
                Text(text = "Frecuencia: ${habit.frequency}")
                Text(text = "Hora del Recordatorio: ${habit.reminderTime}")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}
