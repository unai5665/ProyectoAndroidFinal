package com.example.proyectoandroidfinal

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.proyectoandroidfinal.view.theme.ProyectoAndroidFinalTheme
import com.example.proyectoandroidfinal.view.NavigationGraph
import com.example.proyectoandroidfinal.viewmodel.HabitViewModel

class MainActivity : ComponentActivity() {

    // Activar la solicitud de permisos
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // El permiso fue concedido, ya puedes enviar la notificación
                Toast.makeText(this, "Permiso de notificación concedido", Toast.LENGTH_SHORT).show()
            } else {
                // El permiso fue denegado
                Toast.makeText(this, "Permiso de notificación denegado", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear canal de notificación si es necesario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "habit_channel",
                "Hábito",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal para notificaciones de hábitos"
            }
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Verificar si el permiso ya ha sido concedido
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Si ya se tiene el permiso, muestra la interfaz
                setContent {
                    val navController = rememberNavController()
                    val habitViewModel: HabitViewModel = viewModel()

                    ProyectoAndroidFinalTheme {
                        NavigationGraph(navController = navController, habitViewModel = habitViewModel)
                    }
                }
            } else {
                // Si no se tiene el permiso, solicitarlo
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            setContent {
                val navController = rememberNavController()
                val habitViewModel: HabitViewModel = viewModel()

                ProyectoAndroidFinalTheme {
                    NavigationGraph(navController = navController, habitViewModel = habitViewModel)
                }
            }
        }
    }
}
