package com.example.proyectoandroidfinal.model

import androidx.room.*

interface HabitDao {
    @Insert
    suspend fun insertHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM habit")
    suspend fun getAllHabits(): List<Habit>
}

@Dao
interface ProgressDao {
    @Insert
    suspend fun insertProgress(progress: Progress)

    @Query("SELECT * FROM progress WHERE habitId = :habitId")
    suspend fun getProgressForHabit(habitId: Int): List<Progress>
}

@Dao
interface ReminderDao {
    @Insert
    suspend fun insertReminder(reminder: Reminder)


}