

package com.example.proyectoandroidfinal.view

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyectoandroidfinal.viewmodel.HabitViewModel


@Composable
fun NavigationGraph(navController: NavHostController, habitViewModel: HabitViewModel) {
    NavHost(navController = navController, startDestination = "main_screen") {
        // Pantalla principal
        composable("main_screen") {
            MainScreen(navController = navController, habitViewModel = habitViewModel)
        }

        // Gestión de hábitos
        composable("habit_management") {
            HabitManagementScreen(navController = navController, habitViewModel = habitViewModel)
        }

        // Detalles de un hábito
        composable("habit_detail/{habitId}") { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId")?.toIntOrNull()
            if (habitId != null) {
                HabitDetailScreen(navController = navController, habitId = habitId, habitViewModel = habitViewModel)
            } else {
                // Opcional: Manejo de error si el habitId es inválido
            }
        }
    }
}
