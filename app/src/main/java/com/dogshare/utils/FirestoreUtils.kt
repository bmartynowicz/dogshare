// File: src/main/java/com/dogshare/utils/FirestoreUtils.kt

package com.dogshare.utils

import com.google.firebase.firestore.FirebaseFirestore

// Utility function to check if user preferences exist in Firestore
fun checkUserPreferences(username: String, onResult: (Boolean) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("user_preferences").document(username).get()
        .addOnSuccessListener { document ->
            onResult(document.exists())
        }
        .addOnFailureListener {
            onResult(false)
        }
}
