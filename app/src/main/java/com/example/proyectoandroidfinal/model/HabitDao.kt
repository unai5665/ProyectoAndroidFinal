package com.example.proyectoandroidfinal.model

import androidx.room.*

interface HabitDao {
    @Insert
    suspend fun insertHabit(habit: Habit)
}