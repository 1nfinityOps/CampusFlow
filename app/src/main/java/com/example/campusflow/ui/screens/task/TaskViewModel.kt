package com.example.campusflow.ui.screens.task

import androidx.lifecycle.ViewModel
import com.example.campusflow.data.model.Task
import com.example.campusflow.data.model.TaskStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class TaskViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val tasksCollection = db.collection("tasks")

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        val currentUserId = auth.currentUser?.uid ?: return
        
        // Solo traemos las tareas donde el userId sea igual al del usuario logueado
        tasksCollection.whereEqualTo("userId", currentUserId)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    return@addSnapshotListener
                }
                val updatedTasks = snapshot.documents.mapNotNull { it.toObject(Task::class.java) }
                _tasks.value = updatedTasks
            }
    }

    fun addTask(title: String, subject: String, description: String, status: TaskStatus) {
        val currentUserId = auth.currentUser?.uid ?: return
        val newId = UUID.randomUUID().toString()
        val newTask = Task(
            id = newId,
            userId = currentUserId, // Le asignamos el dueño a la tarea
            title = title,
            subject = subject,
            description = description,
            status = status
        )
        tasksCollection.document(newId).set(newTask)
    }

    fun updateTaskStatus(taskId: String, newStatus: TaskStatus) {
        tasksCollection.document(taskId).update("status", newStatus.name)
    }
}
