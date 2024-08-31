package com.dogshare.ui.utils

import com.google.firebase.firestore.FirebaseFirestore

fun checkUserPreferences(username: String, onResult: (Boolean) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("user_preferences").document(username).get()
        .addOnSuccessListener { document ->
            val hasPreferences = document.exists()
            onResult(hasPreferences)
        }
        .addOnFailureListener {
            onResult(false)
        }
}
