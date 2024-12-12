package com.example.proyectoandroidfinal.model

import androidx.room.Embedded
import androidx.room.Relation

// Clase para representar la relación entre Habit y Reminder
data class HabitWithReminders(
    @Embedded val habit: Habit,  // La entidad Habit
    @Relation(
        parentColumn = "id",  // La columna 'id' de Habit (clave primaria)
        entityColumn = "habitId"  // La columna 'habitId' de Reminder (clave foránea)
    )
    val reminders: List<Reminder>  // La lista de recordatorios relacionados con este hábito
)
