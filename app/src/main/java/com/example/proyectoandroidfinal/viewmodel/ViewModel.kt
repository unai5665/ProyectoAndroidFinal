package com.example.proyectoandroidfinal.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.proyectoandroidfinal.model.AppDatabase
import com.example.proyectoandroidfinal.model.Habit
import com.example.proyectoandroidfinal.model.HabitDao
import com.example.proyectoandroidfinal.model.HabitWithReminders
import com.example.proyectoandroidfinal.model.Progress
import com.example.proyectoandroidfinal.model.Reminder
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application.applicationContext)
    private val habitDao: HabitDao = AppDatabase.getDatabase(application).habitDao()
    private val progressDao = db.progressDao()
    private val reminderDao = db.reminderDao()


    private fun truncateToStartOfDay(timestamp: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    private val _habits = MutableLiveData<List<Habit>>()
    val habits: LiveData<List<Habit>> = _habits

    private val _reminders = MutableLiveData<List<Reminder>>()
    val reminders: LiveData<List<Reminder>> = _reminders

    // Inicialización para cargar hábitos automáticamente
    init {
        loadHabits()
    }

    fun insertReminder(habitId: Int, reminderTime: Long, message: String) {
        viewModelScope.launch {
            val reminder = Reminder(habitId = habitId, reminderTime = reminderTime, message = message)
            reminderDao.insertReminder(reminder)
        }
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

    fun toggleProgress(habitId: Int, date: Long, status: Boolean) {
        viewModelScope.launch {
            val truncatedDate = truncateToStartOfDay(date)
            val existingProgress = progressDao.getProgressByDate(habitId, truncatedDate)

            if (existingProgress == null) {
                // Si no existe progreso, crea uno nuevo
                progressDao.insertProgress(
                    Progress(habitId = habitId, date = truncatedDate, status = status)
                )
            } else if (existingProgress.status != status) {
                // Si ya existe y el estado cambió, actualiza
                progressDao.updateProgress(existingProgress.copy(status = status))
            }
        }
    }


    private fun refreshHabits() {
        viewModelScope.launch {
            // Recargar los hábitos para que la interfaz observe los cambios
            _habits.postValue(habitDao.getAllHabits())
        }
    }


    fun isHabitCompletedLiveData(habitId: Int, date: Long): LiveData<Boolean> {
        val truncatedDate =
            truncateToStartOfDay(date) // Asegura consistencia con las fechas en la base
        return progressDao.getProgressByDateFlow(habitId, truncatedDate).map { progress ->
            progress?.status ?: false // Devuelve false si no hay progreso
        }.asLiveData()
    }
}