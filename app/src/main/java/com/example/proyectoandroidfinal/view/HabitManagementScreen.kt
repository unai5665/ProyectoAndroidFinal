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

}