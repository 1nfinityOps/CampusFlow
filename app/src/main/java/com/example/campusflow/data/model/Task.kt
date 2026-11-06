package com.example.campusflow.data.model

enum class TaskStatus(val label: String) {
    TODO("Por hacer"),
    IN_PROGRESS("En progreso"),
    DONE("Terminado")
}

data class Task(
    val id: String = "",
    val userId: String = "", // Vínculo con el usuario en Firebase
    val title: String = "",
    val subject: String = "",
    val description: String = "",
    val status: TaskStatus = TaskStatus.TODO
)