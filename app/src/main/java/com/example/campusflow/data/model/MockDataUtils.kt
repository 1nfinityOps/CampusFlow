package com.example.campusflow.data.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

object MockDataUtils {

    fun populateFirestoreIfEmpty() {
        val db = FirebaseFirestore.getInstance()

        // Revisar y poblar subjects (Horario)
        db.collection("subjects").get().addOnSuccessListener { snapshot ->
            if (snapshot.isEmpty) {
                Log.d("MockData", "Subjects collection is empty. Populating...")
                val dummySubjects = listOf(
                    Subject(
                        id = UUID.randomUUID().toString(),
                        name = " Desarrollo móvil integral",
                        professor = "M.T.I. Andres De Leon Castrejon",
                        time = "10:00 - 12:00",
                        classroom = "Lab 1",
                        days = listOf("L", "M", "M")
                    ),
                    Subject(
                        id = UUID.randomUUID().toString(),
                        name = "Aplicaciones web progresivas",
                        professor = "MTI. Roberto Jesus Estrada Benavides",
                        time = "9:00 - 10:00",
                        classroom = "Aula 1001",
                        days = listOf("L", "V")
                    ),
                    Subject(
                        id = UUID.randomUUID().toString(),
                        name = "Creación de Videojuegos",
                        professor = "M.C. Ulises Lopez Estrada",
                        time = "7:00 - 9:00",
                        classroom = "Edificio B, Lab 2",
                        days = listOf("L","V")
                    )
                )
                dummySubjects.forEach { subject ->
                    db.collection("subjects").document(subject.id).set(subject)
                }
            }
        }

        // Revisar y poblar notices (Avisos)
        db.collection("notices").get().addOnSuccessListener { snapshot ->
            if (snapshot.isEmpty) {
                Log.d("MockData", "Notices collection is empty. Populating...")
                val dummyNotices = listOf(
                    Notice(
                        id = UUID.randomUUID().toString(),
                        title = "Convocatoria de Becas 2026",
                        description = "Abierta la recepción de documentos para las becas de excelencia académica.",
                        date = "02 Oct 2026",
                        category = "Becas"
                    ),
                    Notice(
                        id = UUID.randomUUID().toString(),
                        title = "Mantenimiento de Servidores",
                        description = "El portal de alumnos no estará disponible este fin de semana por mantenimiento general.",
                        date = "01 Oct 2026",
                        category = "Sistemas"
                    ),
                    Notice(
                        id = UUID.randomUUID().toString(),
                        title = "Feria de Emprendimiento",
                        description = "Acompáñanos este viernes en la explanada central para conocer los proyectos de tus compañeros.",
                        date = "30 Sep 2026",
                        category = "Eventos"
                    )
                )
                dummyNotices.forEach { notice ->
                    db.collection("notices").document(notice.id).set(notice)
                }
            }
        }
    }
}
