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
}

