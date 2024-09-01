package com.dogshare.utils

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

object FirestoreUtils {

    private val db = FirebaseFirestore.getInstance()

    // Using suspend function and coroutines for better asynchronous handling
    suspend fun checkUserPreferences(username: String, dispatcher: CoroutineDispatcher = Dispatchers.IO): Boolean = withContext(dispatcher) {
        if (username.isBlank()) return@withContext false

        try {
            val documentSnapshot = db.collection("user_preferences").document(username).get().await()
            documentSnapshot.exists()
        } catch (e: Exception) {
            // Log error or handle it as per application's error handling policy
            false
        }
    }
}
