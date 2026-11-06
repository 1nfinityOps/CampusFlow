package com.example.campusflow.ui.screens.notices

import androidx.lifecycle.ViewModel
import com.example.campusflow.data.model.Notice
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoticesViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val noticesCollection = db.collection("notices")

    private val _notices = MutableStateFlow<List<Notice>>(emptyList())
    val notices: StateFlow<List<Notice>> = _notices.asStateFlow()

    private var isFirstLoad = true

    private val _newNoticeEvent = MutableStateFlow<Notice?>(null)
    val newNoticeEvent: StateFlow<Notice?> = _newNoticeEvent.asStateFlow()

    init {
        fetchNotices()
    }

    private fun fetchNotices() {
        noticesCollection.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener
            
            val currentNotices = snapshot.documents.mapNotNull { it.toObject(Notice::class.java) }
            
            // Lógica para detectar si se agregó un aviso nuevo mientras la app está abierta
            if (!isFirstLoad) {
                val oldSize = _notices.value.size
                val newSize = currentNotices.size
                if (newSize > oldSize) {
                    // Hay un aviso nuevo, buscamos el que no estaba antes
                    val newNotice = currentNotices.firstOrNull { notice -> 
                        !_notices.value.any { oldNotice -> oldNotice.id == notice.id } 
                    }
                    if (newNotice != null) {
                        _newNoticeEvent.value = newNotice
                    }
                }
            }
            
            _notices.value = currentNotices
            isFirstLoad = false
        }
    }

    fun consumeNewNoticeEvent() {
        _newNoticeEvent.value = null
    }

    fun addNotice(title: String, category: String, description: String) {
        val newId = UUID.randomUUID().toString()
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())

        val newNotice = Notice(
            id = newId,
            title = title,
            description = description,
            date = currentDate,
            category = category
        )
        noticesCollection.document(newId).set(newNotice)
    }

    fun deleteNotice(noticeId: String) {
        noticesCollection.document(noticeId).delete()
    }
}
