package com.dogshare.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class PreferencesRepository(context: Context) {

    companion object {
        private const val PREFS_NAME = "app_preferences"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_DARK_MODE_ENABLED = "dark_mode_enabled"
        private const val KEY_ACCOUNT_PRIVACY_ENABLED = "account_privacy_enabled"
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
    fun getPromptLogin(): Boolean = sharedPreferences.getBoolean(PROMPT_LOGIN_KEY, true)
    fun getLastLoginTimestamp(): Long = sharedPreferences.getLong(LAST_LOGIN_TIMESTAMP_KEY, 0L)

    fun getUserId(): String? {
        val userId = sharedPreferences.getString(KEY_USER_ID, null)
        Log.d("PreferencesRepository", "Retrieved userId: $userId") // Log statement
        return userId
    }

    // Setters for preferences
    fun setPromptLogin(prompt: Boolean) {
        sharedPreferences.edit().putBoolean(PROMPT_LOGIN_KEY, prompt).apply()
    }

    fun updateLastLoginTimestamp() {
        val timestamp = System.currentTimeMillis()
        sharedPreferences.edit().putLong(LAST_LOGIN_TIMESTAMP_KEY, timestamp).apply()
        Log.d("PreferencesRepository", "Updated last login timestamp: $timestamp")
    }

    fun setUserId(userId: String) {
        sharedPreferences.edit().putString(KEY_USER_ID, userId).apply()
        updateLastLoginTimestamp()
        Log.d("PreferencesRepository", "UserId set: $userId") // Log statement
    }

    // Clear user preferences on logout
    fun clearUserId() {
        sharedPreferences.edit().remove(KEY_USER_ID).apply()
        setPromptLogin(true)  // Ensure prompt login is set after clearing user ID
        Log.d("PreferencesRepository", "Cleared userId")
    }

    // Additional preference methods like Notifications, DarkMode, etc.
    fun getNotificationsEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, false)
    }

    fun setNotificationsEnabled(value: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, value).apply()
    }

    fun getDarkModeEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_DARK_MODE_ENABLED, false)
    }

    fun setDarkModeEnabled(value: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_DARK_MODE_ENABLED, value).apply()
    }

    fun getAccountPrivacyEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_ACCOUNT_PRIVACY_ENABLED, false)
    }

    fun setAccountPrivacyEnabled(value: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_ACCOUNT_PRIVACY_ENABLED, value).apply()
    }
}
