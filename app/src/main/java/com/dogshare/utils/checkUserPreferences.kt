package com.dogshare.ui.utils

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

// Assuming usage of Kotlin Coroutines for asynchronous tasks
suspend fun checkUserPreferences(userId: String, dispatcher: CoroutineDispatcher = Dispatchers.IO): Boolean = withContext(dispatcher) {
    if (userId.isBlank()) return@withContext false

    val db = FirebaseFirestore.getInstance()
    try {
        val documentSnapshot = db.collection("user_preferences").document(userId).get().await()
        documentSnapshot.exists()
    } catch (e: Exception) {
        // Log the exception or handle it as necessary
        false
    }
}
