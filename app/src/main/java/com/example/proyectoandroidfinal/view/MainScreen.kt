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
    val today = System.currentTimeMillis() // Fecha actual en milisegundos

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Hábitos") },
                actions = {
                    IconButton(onClick = {
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
                // Aquí no es necesario hacer la comprobación del progreso directamente,
                // ya que esto se manejará dentro del HabitItem.
                HabitItem(
                    habit = habit,
                    habitViewModel = habitViewModel,
                    today = today,
                    onClick = { navController.navigate("habit_detail/${habit.id}") }
                )
            }
        }
    }
}

@Composable
fun HabitItem(habit: Habit, habitViewModel: HabitViewModel, today: Long, onClick: () -> Unit) {
    // Observando el progreso del hábito para la fecha actual
    val isCompleted = habitViewModel.isHabitCompletedLiveData(habit.id, today).observeAsState(false)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = habit.name, fontWeight = FontWeight.Bold)
                Text(text = "Categoría: ${habit.category}")
                Text(text = "Frecuencia: ${habit.frequency}")
            }

            // Checkbox para marcar si el hábito fue completado
            Checkbox(
                checked = isCompleted.value,
                onCheckedChange = { isChecked ->
                    // Cambiar el estado del progreso
                    habitViewModel.toggleProgress(habit.id, today, isChecked)
                }
            )
        }
    }
}
