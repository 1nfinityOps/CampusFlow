package com.example.campusflow.data.model

data class Subject(
    val id: String = "",
    val userId: String = "", // Para saber de qué alumno es la materia
    val name: String = "",
    val professor: String = "",
    val time: String = "",
    val classroom: String = "",
    val days: List<String> = emptyList()
)
