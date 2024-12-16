package com.example.proyectoandroidfinal.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.proyectoandroidfinal.model.AppDatabase
import com.example.proyectoandroidfinal.model.Habit
import com.example.proyectoandroidfinal.model.HabitDao
import com.example.proyectoandroidfinal.model.HabitWithReminders
import com.example.proyectoandroidfinal.model.Reminder
import kotlinx.coroutines.launch

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application.applicationContext)
    private val habitDao: HabitDao = AppDatabase.getDatabase(application).habitDao()
    private val progressDao = db.progressDao()
    private val reminderDao = db.reminderDao()

    private val _habits = MutableLiveData<List<Habit>>()
    val habits: LiveData<List<Habit>> = _habits

    private val _reminders = MutableLiveData<List<Reminder>>()
    val reminders: LiveData<List<Reminder>> = _reminders

    // Inicialización para cargar hábitos automáticamente
    init {
        loadHabits()
    }

    fun loadHabits() {
        viewModelScope.launch {
            _habits.value = habitDao.getAllHabits()
        }
    }

    // Función para cargar recordatorios de un hábito
    fun loadRemindersForHabit(habitId: Int) {
        viewModelScope.launch {
            _reminders.value = reminderDao.getRemindersForHabit(habitId)
        }
    }

    // Función para obtener los hábitos con sus recordatorios
    val habitsWithReminders: LiveData<List<HabitWithReminders>> = liveData {
        val data = habitDao.getHabitsWithReminders()  // Llamamos al DAO para obtener los datos
        emit(data)
    }

    // Función para insertar un nuevo hábito
    fun insertHabit(habit: Habit) {
        viewModelScope.launch {
            habitDao.insertHabit(habit)
            loadHabits()  // Recargar los hábitos después de la inserción
        }
    }

    // Función para actualizar un hábito existente
    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
            habitDao.updateHabit(habit)
            loadHabits()  // Recargar los hábitos después de la actualización
        }
    }

    // Función para eliminar un hábito
    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            habitDao.deleteHabit(habit)
            loadHabits()  // Recargar los hábitos después de la eliminación
        }
    }
}
