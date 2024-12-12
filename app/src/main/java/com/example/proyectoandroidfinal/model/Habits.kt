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

@Entity(tableName = "progress",
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Progress(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val habitId: Int,  // Relación con la tabla Habit
    val date: Long,
    val status: Boolean,  // True si el hábito fue completado, False si no
    val note: String? = null
)