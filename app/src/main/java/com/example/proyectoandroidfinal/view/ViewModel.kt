package com.example.proyectoandroidfinal.view

import android.app.*
import androidx.lifecycle.*
import com.example.proyectoandroidfinal.model.AppDatabase
import com.example.proyectoandroidfinal.model.Habit
import com.example.proyectoandroidfinal.model.Reminder
import kotlinx.coroutines.*

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application.applicationContext)
    private val habitDao = db.habitDao()
    private val progressDao = db.progressDao()
    private val reminderDao = db.reminderDao()

    // LiveData para mantener los datos
    private val _habits = MutableLiveData<List<Habit>>()
    val habits: LiveData<List<Habit>> = _habits

    private val _reminders = MutableLiveData<List<Reminder>>()
    val reminders: LiveData<List<Reminder>> = _reminders

    // Función para cargar hábitos
    fun loadHabits() {
        viewModelScope.launch {
            _habits.value = habitDao.getAllHabits()
        }
    }


}