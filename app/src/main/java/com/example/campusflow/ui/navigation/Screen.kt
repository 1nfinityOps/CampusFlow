package com.example.campusflow.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Tasks : Screen("tasks", "Tareas", Icons.Default.Assignment)
    object Schedule : Screen("schedule", "Horario", Icons.Default.DateRange)
    object Notices : Screen("notices", "Avisos", Icons.Default.Notifications)
    object Profile : Screen("profile", "Perfil", Icons.Default.Person)
}
