package com.dogshare.ui.utils

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

suspend fun checkUserPreferences(userId: String, dispatcher: CoroutineDispatcher = Dispatchers.IO): Boolean {
    return withContext(dispatcher) {
        val db = FirebaseFirestore.getInstance()
        val userPreferencesRef = db.collection("user_preferences").document(userId)
        try {
            val document = userPreferencesRef.get().await()
            document != null && document.exists()
        } catch (e: Exception) {
            // Handle the exception
            false
        }
    }
}
