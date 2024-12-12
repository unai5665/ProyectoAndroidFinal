package com.example.proyectoandroidfinal.model

import androidx.room.*

@Entity(tableName = "habit")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,
    val frequency: String,
    val reminderTime: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "progress")
data class Progress(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val habitId: Int,
    val date: Long,
    val status: Boolean,
    val note: String? = null
)