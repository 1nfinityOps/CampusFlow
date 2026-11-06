package com.example.campusflow.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow

// Estado Global del Tema Oscuro/Claro
val isAppInDarkTheme = MutableStateFlow(true)

fun Modifier.liquidGlass(cornerRadius: Int = 20): Modifier {
    return this
        .clip(RoundedCornerShape(cornerRadius.dp))
        .background(Color(0x15FFFFFF)) // Fondo semi transparente para simular cristal
        .border(
            width = 1.dp,
            color = Color(0x30FFFFFF), // Borde ligero blanco
            shape = RoundedCornerShape(cornerRadius.dp)
        )
}

fun Modifier.animatedLiquidBackground(): Modifier = composed {
    val isDark by isAppInDarkTheme.collectAsState()
    val infiniteTransition = rememberInfiniteTransition(label = "liquid_bg")
    
    // Animación en el eje X de inicio
    val startX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "startX"
    )
    
    // Animación en el eje Y de inicio
    val startY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1500f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "startY"
    )

    // Animación en el eje X de final
    val endX by infiniteTransition.animateFloat(
        initialValue = 1500f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "endX"
    )

    // Animación en el eje Y de final
    val endY by infiniteTransition.animateFloat(
        initialValue = 2000f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "endY"
    )

    val animatedBrush = Brush.linearGradient(
        colors = if (isDark) {
            listOf(
                Color(0xFF0F2027), // Deep Dark Blue
                Color(0xFF203A43), // Cyan obscuro
                Color(0xFF2C5364), // Acero
                Color(0xFF0F2027)  // Regreso al obscuro
            )
        } else {
            listOf(
                Color(0xFFFF7E5F), // Naranja Atardecer
                Color(0xFFFEB47B), // Durazno
                Color(0xFFFF9966), // Coral
                Color(0xFFFF7E5F)  // Regreso
            )
        },
        start = Offset(startX, startY),
        end = Offset(endX, endY)
    )
    
    this.background(animatedBrush)
}
