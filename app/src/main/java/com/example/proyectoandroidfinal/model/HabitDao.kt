package com.example.proyectoandroidfinal.model

import androidx.room.*

@Dao
interface HabitDao {
    @Insert
    suspend fun insertHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM habit")
    suspend fun getAllHabits(): List<Habit>

    // Consulta para obtener todos los hábitos con sus recordatorios
    @Transaction
    @Query("SELECT * FROM habit")
    suspend fun getHabitsWithReminders(): List<HabitWithReminders>

}

@Dao
interface ProgressDao {


    @Update
    suspend fun updateProgress(progress: Progress)
    @Insert
    suspend fun insertProgress(progress: Progress)

    // Obtener progreso de un hábito por fecha
    @Query("SELECT * FROM progress WHERE habitId = :habitId AND date = :date")
    suspend fun getProgressByDate(habitId: Int, date: Long): Progress?

    @Query("SELECT * FROM progress WHERE habitId = :habitId")
    suspend fun getProgressForHabit(habitId: Int): List<Progress>

}

@Dao
interface ReminderDao {
    @Insert
    suspend fun insertReminder(reminder: Reminder)

    @Query("SELECT * FROM reminder WHERE habitId = :habitId")
    suspend fun getRemindersForHabit(habitId: Int): List<Reminder>

    @Query("SELECT * FROM reminder")
    suspend fun getAllReminders(): List<Reminder>
}