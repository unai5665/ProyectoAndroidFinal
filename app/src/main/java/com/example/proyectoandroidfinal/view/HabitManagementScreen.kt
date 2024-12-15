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

        }
    }
}

