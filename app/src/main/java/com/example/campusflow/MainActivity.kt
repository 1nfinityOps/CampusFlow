package com.example.campusflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.campusflow.ui.screens.MainScreen
import com.example.campusflow.ui.screens.auth.LoginScreen
import com.example.campusflow.ui.theme.CampusFlowTheme
import com.example.campusflow.data.model.MockDataUtils
import com.google.firebase.auth.FirebaseAuth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Habilitar Persistencia Offline en Firestore para que funcione sin internet (Equivalente a Room)
        val settings = FirebaseFirestoreSettings.Builder()
            .setLocalCacheSettings(PersistentCacheSettings.newBuilder().build())
            .build()
        FirebaseFirestore.getInstance().firestoreSettings = settings

        // Cargar datos de prueba automáticamente en Firestore si está vacío
        MockDataUtils.populateFirestoreIfEmpty()

        setContent {
            CampusFlowTheme {
                var isUserLoggedIn by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser != null) }

                AnimatedContent(
                    targetState = isUserLoggedIn,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(800)) togetherWith fadeOut(animationSpec = tween(800))
                    },
                    label = "LoginTransition"
                ) { loggedIn ->
                    if (loggedIn) {
                        MainScreen(
                            onLogout = {
                                FirebaseAuth.getInstance().signOut()
                                isUserLoggedIn = false // Dispara la recomposición hacia LoginScreen
                            }
                        )
                    } else {
                        LoginScreen(
                            onLoginSuccess = { isUserLoggedIn = true }
                        )
                    }
                }
            }
        }
    }
}



