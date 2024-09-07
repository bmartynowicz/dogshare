package com.dogshare.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PreferencesRepository(context: Context, private val firestore: FirebaseFirestore) {

    // Constants for preference keys
    companion object {
        private const val PREFS_NAME = "app_preferences"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications"
        private const val KEY_DARK_MODE_ENABLED = "dark_mode"
        private const val KEY_ACCOUNT_PRIVACY_ENABLED = "account_privacy"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_ID = "user_id"
        private const val PROMPT_LOGIN_KEY = "prompt_login"
        private const val LAST_LOGIN_TIMESTAMP_KEY = "last_login_timestamp"
    }

    // Initialize encrypted SharedPreferences
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // Getters for preferences
    fun getNotificationsEnabled(): Boolean = sharedPreferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, false)
    fun getDarkModeEnabled(): Boolean = sharedPreferences.getBoolean(KEY_DARK_MODE_ENABLED, false)
    fun getAccountPrivacyEnabled(): Boolean = sharedPreferences.getBoolean(KEY_ACCOUNT_PRIVACY_ENABLED, false)
    fun getUserEmail(): String? = sharedPreferences.getString(KEY_USER_EMAIL, null)
    fun getUserId(): String? = sharedPreferences.getString(KEY_USER_ID, null)
    fun getPromptLogin(): Boolean = sharedPreferences.getBoolean(PROMPT_LOGIN_KEY, false)
    fun getLastLoginTimestamp(): Long = sharedPreferences.getLong(LAST_LOGIN_TIMESTAMP_KEY, 0L)

    // Setters for preferences
    fun setNotificationsEnabled(isEnabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, isEnabled).apply()
    }

    fun setDarkModeEnabled(isEnabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_DARK_MODE_ENABLED, isEnabled).apply()
    }

    fun setAccountPrivacyEnabled(isEnabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_ACCOUNT_PRIVACY_ENABLED, isEnabled).apply()
    }

    fun setUserEmail(email: String) {
        sharedPreferences.edit().putString(KEY_USER_EMAIL, email).apply()
    }

    fun setUserId(userId: String) {
        sharedPreferences.edit().putString(KEY_USER_ID, userId).apply()
    }

    fun setPromptLogin(prompt: Boolean) {
        sharedPreferences.edit().putBoolean(PROMPT_LOGIN_KEY, prompt).apply()
    }

    fun updateLastLoginTimestamp() {
        val timestamp = System.currentTimeMillis()
        sharedPreferences.edit().putLong(LAST_LOGIN_TIMESTAMP_KEY, timestamp).apply()
        Log.d("PreferencesRepository", "Updated last login timestamp: $timestamp")
    }

    // Clear user preferences (for logout)
    fun clearUserId() {
        sharedPreferences.edit().remove(KEY_USER_ID).apply()
        Log.d("PreferencesRepository", "Cleared userId")
    }

    // Firestore functions to handle user preferences in the cloud
    suspend fun saveUserEmailToFirestore(email: String) {
        try {
            val userDocument = firestore.collection("users").document(email)
            userDocument.set(mapOf("email" to email)).await()
            setUserEmail(email)  // Save to SharedPreferences as well
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun fetchUserEmailFromFirestore(email: String): String? {
        return try {
            val documentSnapshot = firestore.collection("users").document(email).get().await()
            documentSnapshot.getString("email")
        } catch (e: Exception) {
            null
        }
    }
}