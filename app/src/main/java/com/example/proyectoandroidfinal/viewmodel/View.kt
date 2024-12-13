package com.example.proyectoandroidfinal.viewmodel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.proyectoandroidfinal.view.HabitViewModel

@Composable
fun HabitScreen(habitViewModel: HabitViewModel) {
    val habits by habitViewModel.habits.observeAsState(emptyList())
    val reminders by habitViewModel.reminders.observeAsState(emptyList())

    // Mostrar los hábitos
    LazyColumn {
        items(habits) { habit ->
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = habit.name, fontWeight = FontWeight.Bold)
                Text(text = habit.category)
                // Mostrar los recordatorios de este hábito
                reminders.filter { it.habitId == habit.id }.forEach { reminder ->
                    Text(text = "Reminder: ${reminder.reminderTime}")
                }
            }
        }
    }
}
