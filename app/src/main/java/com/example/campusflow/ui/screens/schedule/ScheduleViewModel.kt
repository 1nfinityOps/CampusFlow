package com.example.campusflow.ui.screens.schedule

import androidx.lifecycle.ViewModel
import com.example.campusflow.data.model.Subject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class ScheduleViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val subjectsCollection = db.collection("subjects")

    private val _subjects = MutableStateFlow<List<Subject>>(emptyList())
    val subjects: StateFlow<List<Subject>> = _subjects.asStateFlow()

    private val _adminProfessors = MutableStateFlow<List<String>>(emptyList())
    val adminProfessors: StateFlow<List<String>> = _adminProfessors.asStateFlow()

    init {
        val email = auth.currentUser?.email ?: ""
        if (email.endsWith("@admin.ut.edu.mx")) {
            fetchAllProfessors()
        } else {
            fetchStudentSubjects()
        }
    }

    private fun fetchStudentSubjects() {
        val userId = auth.currentUser?.uid ?: return
        subjectsCollection.whereEqualTo("userId", userId).addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener
            _subjects.value = snapshot.documents.mapNotNull { it.toObject(Subject::class.java) }
        }
    }

    private fun fetchAllProfessors() {
        // Para administradores: Extrae la lista única de profesores de TODAS las materias registradas
        subjectsCollection.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener
            val allSubjects = snapshot.documents.mapNotNull { it.toObject(Subject::class.java) }
            _adminProfessors.value = allSubjects.map { it.professor }.filter { it.isNotBlank() }.distinct().sorted()
        }
    }

    fun addSubject(name: String, professor: String, time: String, classroom: String, days: List<String>) {
        val userId = auth.currentUser?.uid ?: return
        val newId = UUID.randomUUID().toString()
        val newSubj = Subject(newId, userId, name, professor, time, classroom, days)
        subjectsCollection.document(newId).set(newSubj)
    }
}
