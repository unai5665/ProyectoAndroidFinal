package com.example.proyectoandroidfinal.view

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proyectoandroidfinal.model.Habit
import com.example.proyectoandroidfinal.viewmodel.HabitViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderScreen(
    navController: NavController,
    habitViewModel: HabitViewModel
) {
    val habitList = habitViewModel.habits.observeAsState(emptyList()).value
    val selectedHabit = remember { mutableStateOf<Habit?>(null) }
    val reminderTime = remember { mutableStateOf<Long>(System.currentTimeMillis()) }
    val reminderMessage = remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir Recordatorio") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Selección del hábito
                Text("Selecciona un hábito:")
                Box(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = selectedHabit.value?.name ?: "Selecciona un hábito",
                        onValueChange = {}, // Este campo no se necesita modificar
                        readOnly = true, // Hacerlo no editable
                        trailingIcon = {
                            IconButton(onClick = { expanded.value = !expanded.value }) {
                                Icon(Icons.Filled.ArrowDropDown, contentDescription = "Expandir")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        habitList.forEach { habit ->
                            DropdownMenuItem(
                                text = { Text(habit.name) },
                                onClick = {
                                    selectedHabit.value = habit
                                    expanded.value = false
                                }
                            )
                        }
                    }
                }

                // Selección de hora del recordatorio
                Text("Selecciona la hora del recordatorio:")
                TimePicker(
                    time = reminderTime.value,
                    onTimeChanged = { newTime -> reminderTime.value = newTime }
                )

                // Campo para el mensaje del recordatorio
                Text("Mensaje del recordatorio:")
                TextField(
                    value = reminderMessage.value,
                    onValueChange = { reminderMessage.value = it },
                    placeholder = { Text("Escribe tu mensaje") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Botón para guardar el recordatorio
                Button(
                    onClick = {
                        if (selectedHabit.value != null && reminderMessage.value.isNotEmpty()) {
                            habitViewModel.insertReminder(
                                selectedHabit.value!!.id,
                                reminderTime.value,
                                reminderMessage.value
                            )
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Guardar Recordatorio")
                }
            }
        }
    )
}

@Composable
fun TimePicker(time: Long, onTimeChanged: (Long) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance().apply { timeInMillis = time }
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    Button(onClick = {
        TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                val updatedCalendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                onTimeChanged(updatedCalendar.timeInMillis)
            },
            hour,
            minute,
            true
        ).show()
    }) {
        Text("Seleccionar Hora")
    }
}
