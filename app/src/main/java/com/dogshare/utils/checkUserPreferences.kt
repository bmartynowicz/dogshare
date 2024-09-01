package com.dogshare.ui.utils

import com.google.firebase.firestore.FirebaseFirestore

fun checkUserPreferences(userId: String, onResult: (Boolean) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    if (userId.isNotBlank()) {
        val docRef = db.collection("user_preferences").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                val hasPreferences = document.exists()
                onResult(hasPreferences)
            }
            .addOnFailureListener { e ->
                onResult(false)
            }
    } else {
        onResult(false)
    }
}
