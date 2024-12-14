

package com.example.proyectoandroidfinal.view

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(navController = navController)
        }
        composable("habit_management") {
            HabitManagementScreen(habitViewModel = viewModel()) // La pantalla para gestionar hábitos
        }
        composable("habit_detail/{habitId}") { backStackEntry ->
            val habitId = backStackEntry.arguments?.getString("habitId")
            // Aquí deberías navegar a una pantalla de detalles de un hábito, si la tienes
        }
    }
}
