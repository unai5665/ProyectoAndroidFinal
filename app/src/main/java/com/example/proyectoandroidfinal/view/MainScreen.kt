package com.example.proyectoandroidfinal.view


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectoandroidfinal.model.Habit
import com.example.proyectoandroidfinal.viewmodel.HabitViewModel


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MainScreen(navController: NavController, habitViewModel: HabitViewModel) {
    val habits = habitViewModel.habits.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Hábitos") },
                actions = {
                    IconButton(onClick = {
                        // Navegar a la pantalla de gestión para agregar un nuevo hábito
                        navController.navigate("habit_management")
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar Hábito")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(habits.value) { habit ->
                HabitItem(habit = habit) {
                    // Navegar a la pantalla de detalles del hábito con el ID del hábito
                    navController.navigate("habit_detail/${habit.id}")
                }
            }
        }
    }
}

@Composable
fun HabitItem(habit: Habit, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = habit.name, fontWeight = FontWeight.Bold)
            Text(text = "Categoría: ${habit.category}")
            Text(text = "Frecuencia: ${habit.frequency}")
        }
    }
}
