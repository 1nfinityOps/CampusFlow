package com.example.campusflow.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.campusflow.ui.components.animatedLiquidBackground
import com.example.campusflow.ui.navigation.Screen
import com.example.campusflow.ui.screens.notices.NoticesScreen
import com.example.campusflow.ui.screens.profile.ProfileScreen
import com.example.campusflow.ui.screens.schedule.ScheduleScreen
import com.example.campusflow.ui.screens.task.TasksScreen

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campusflow.ui.screens.notices.NoticesViewModel

@Composable
fun MainScreen(
    onLogout: () -> Unit,
    noticesViewModel: NoticesViewModel = viewModel()
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val newNotice by noticesViewModel.newNoticeEvent.collectAsState()

    // Detectar cuando hay un nuevo aviso y mostrar el Snackbar
    LaunchedEffect(newNotice) {
        newNotice?.let { notice ->
            snackbarHostState.showSnackbar(
                message = "📢 Nuevo Aviso: ${notice.title}",
                actionLabel = "Ver",
                duration = SnackbarDuration.Long
            )
            // Marcar como consumido para que no vuelva a salir
            noticesViewModel.consumeNewNoticeEvent()
            
            // Navegar a la pantalla de avisos al darle clic a "Ver"
            navController.navigate(Screen.Notices.route) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    val items = listOf(
        Screen.Tasks,
        Screen.Schedule,
        Screen.Notices,
        Screen.Profile
    )

    Box(modifier = Modifier.fillMaxSize().animatedLiquidBackground()) {
        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { 
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = Color(0x90000000), // Fondo muy oscuro
                        contentColor = Color.White,
                        actionColor = Color(0xFF00C9FF) // Cyan para el botón "Ver"
                    )
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0x05FFFFFF),
                    contentColor = Color.White
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
    
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF00C9FF),
                                unselectedIconColor = Color.White.copy(alpha = 0.6f),
                                selectedTextColor = Color(0xFF00C9FF),
                                unselectedTextColor = Color.White.copy(alpha = 0.6f),
                                indicatorColor = Color(0x20FFFFFF)
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Tasks.route,
                modifier = Modifier.padding(innerPadding),
                enterTransition = { 
                    fadeIn(animationSpec = tween(400)) + scaleIn(initialScale = 0.95f, animationSpec = tween(400)) 
                },
                exitTransition = { 
                    fadeOut(animationSpec = tween(400)) + scaleOut(targetScale = 1.05f, animationSpec = tween(400)) 
                }
            ) {
                composable(Screen.Tasks.route) {
                    TasksScreen()
                }
                composable(Screen.Schedule.route) {
                    ScheduleScreen()
                }
                composable(Screen.Notices.route) {
                    NoticesScreen()
                }
                composable(Screen.Profile.route) {
                    ProfileScreen(onLogout = onLogout)
                }
            }
        }
    }
}
