package com.example.campusflow.ui.screens.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.campusflow.ui.components.liquidGlass

@Composable
fun GradeCalculatorDialog(
    onDismiss: () -> Unit
) {
    var partial1 by remember { mutableStateOf("") }
    var partial2 by remember { mutableStateOf("") }
    var passingGradeStr by remember { mutableStateOf("70") }

    val currentAverage = try {
        val p1 = partial1.toFloatOrNull() ?: 0f
        val p2 = partial2.toFloatOrNull() ?: 0f
        (p1 + p2) / 2
    } catch (e: Exception) { 0f }

    val neededForFinal = try {
        val p1 = partial1.toFloatOrNull() ?: 0f
        val p2 = partial2.toFloatOrNull() ?: 0f
        val passingGrade = passingGradeStr.toFloatOrNull() ?: 70f
        // Suponiendo 3 parciales en total, ¿cuánto necesito en el 3ero?
        (passingGrade * 3) - p1 - p2
    } catch (e: Exception) { 0f }

    val isP1Valid = partial1.isBlank() || (partial1.toFloatOrNull()?.let { it in 0f..100f } == true)
    val isP2Valid = partial2.isBlank() || (partial2.toFloatOrNull()?.let { it in 0f..100f } == true)

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.liquidGlass(),
        containerColor = Color(0x30000000), // Fondo muy oscuro pero transparente
        title = { Text("Simulador de Calificaciones", color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Calcula cuánto necesitas en el último parcial para aprobar (Asumiendo 3 parciales).",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
                
                OutlinedTextField(
                    value = partial1,
                    onValueChange = { partial1 = it },
                    label = { Text("Parcial 1 (Ej. 85)") },
                    singleLine = true,
                    isError = !isP1Valid,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00C9FF),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedLabelColor = Color(0xFF00C9FF),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        errorBorderColor = Color(0xFFFF5252),
                        errorLabelColor = Color(0xFFFF5252)
                    )
                )
                if (!isP1Valid) {
                    Text("Valor inválido (0-100)", color = Color(0xFFFF5252), style = MaterialTheme.typography.labelSmall)
                }
                
                OutlinedTextField(
                    value = partial2,
                    onValueChange = { partial2 = it },
                    label = { Text("Parcial 2 (Ej. 60)") },
                    singleLine = true,
                    isError = !isP2Valid,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00C9FF),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedLabelColor = Color(0xFF00C9FF),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        errorBorderColor = Color(0xFFFF5252),
                        errorLabelColor = Color(0xFFFF5252)
                    )
                )
                if (!isP2Valid) {
                    Text("Valor inválido (0-100)", color = Color(0xFFFF5252), style = MaterialTheme.typography.labelSmall)
                }

                OutlinedTextField(
                    value = passingGradeStr,
                    onValueChange = { passingGradeStr = it },
                    label = { Text("Calificación Mínima Aprobatoria") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF00C9FF),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedLabelColor = Color(0xFF00C9FF),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (partial1.isNotBlank() || partial2.isNotBlank()) {
                    Text(
                        text = "Promedio Actual (de 2): %.1f".format(currentAverage),
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )

                    if (neededForFinal > 100) {
                        Text(
                            text = "Necesitas %.1f en el P3 (¡Es imposible aprobar con 3 parciales!)".format(neededForFinal),
                            color = Color(0xFFFF5252), // Rojo brillante
                            fontWeight = FontWeight.Bold
                        )
                    } else if (neededForFinal <= 0) {
                        Text(
                            text = "¡Ya estás aprobado!",
                            color = Color(0xFF69F0AE), // Verde neón
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "Necesitas %.1f en el Parcial 3 para pasar.".format(neededForFinal),
                            color = Color(0xFFFFD740), // Amarillo neón
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar", color = Color(0xFF00C9FF))
            }
        }
    )
}
