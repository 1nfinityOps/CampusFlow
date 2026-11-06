package com.example.campusflow.ui.screens.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campusflow.ui.components.SubjectCard
import com.example.campusflow.ui.components.liquidGlass
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    scheduleViewModel: ScheduleViewModel = viewModel()
) {
    val subjects by scheduleViewModel.subjects.collectAsState()
    val adminProfessors by scheduleViewModel.adminProfessors.collectAsState()
    var showCalculator by remember { mutableStateOf(false) }
    var showAddSubjectDialog by remember { mutableStateOf(false) }

    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
    val isAdmin = currentUserEmail.endsWith("@admin.ut.edu.mx")

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text(if (isAdmin) "Directorio de Profesores" else "Horario de Clases", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                actions = {
                    if (!isAdmin) {
                        IconButton(onClick = { showCalculator = true }) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = "Simulador de Calificaciones",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (!isAdmin) {
                FloatingActionButton(
                    onClick = { showAddSubjectDialog = true },
                    containerColor = Color(0xFF00C9FF),
                    contentColor = Color.Black
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar Materia")
                }
            }
        }
    ) { innerPadding ->
        if (isAdmin) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ) {
                items(adminProfessors) { prof ->
                    Card(
                        modifier = Modifier.fillMaxWidth().liquidGlass(),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF00C9FF))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(text = prof, color = Color.White, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ) {
                items(subjects, key = { it.id }) { subject ->
                    SubjectCard(subject = subject)
                }
            }
        }
        
        if (showCalculator) {
            GradeCalculatorDialog(onDismiss = { showCalculator = false })
        }

        if (showAddSubjectDialog) {
            AddSubjectDialog(
                onDismiss = { showAddSubjectDialog = false },
                onConfirm = { name, prof, time, classroom, days ->
                    scheduleViewModel.addSubject(name, prof, time, classroom, days)
                    showAddSubjectDialog = false
                }
            )
        }
    }
}

@Composable
fun AddSubjectDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, prof: String, time: String, classroom: String, days: List<String>) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var prof by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var classroom by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.liquidGlass(),
        containerColor = Color(0x30000000),
        title = { Text("Nueva Materia", color = Color.White) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name, onValueChange = { name = it }, label = { Text("Nombre de la materia") },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                OutlinedTextField(
                    value = prof, onValueChange = { prof = it }, label = { Text("Profesor") },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                OutlinedTextField(
                    value = time, onValueChange = { time = it }, label = { Text("Horario (Ej. 10:00 - 12:00)") },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
                OutlinedTextField(
                    value = classroom, onValueChange = { classroom = it }, label = { Text("Aula") },
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                )
            }
        },
        confirmButton = {
            Button(onClick = { 
                if (name.isNotBlank()) onConfirm(name, prof, time, classroom, listOf("L", "M", "X", "J", "V")) 
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = Color.White.copy(alpha = 0.7f)) }
        }
    )
}
