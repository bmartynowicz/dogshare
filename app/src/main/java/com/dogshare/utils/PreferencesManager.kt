package com.dogshare.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.util.*

object PreferencesManager {

    private const val PREF_NAME = "app_preferences"
    private const val USER_ID_KEY = "user_id"
    private const val PROMPT_LOGIN_KEY = "prompt_login"
    private const val LAST_LOGIN_TIMESTAMP_KEY = "last_login_timestamp"
    private lateinit var preferences: SharedPreferences  // Updated type

    /**
     * Initializes the PreferencesManager with the provided context using EncryptedSharedPreferences.
     */
    fun initialize(context: Context) {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        preferences = EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        Log.d("PreferencesManager", "PreferencesManager initialized with encrypted preferences")
    }

    /**
     * Retrieves the saved user ID from SharedPreferences.
     * @return the user ID or null if not found.
     */
    fun getUserId(): String? {
        val userId = preferences.getString(USER_ID_KEY, null)
        Log.d("PreferencesManager", "Retrieved userId: $userId")
        return userId
    }

    /**
     * Saves the user ID to SharedPreferences.
     * @param userId the user ID to save.
     */
    fun saveUserId(userId: String) {
        with(preferences.edit()) {
            putString(USER_ID_KEY, userId)
            apply()
        }
        Log.d("PreferencesManager", "Saved userId: $userId")
    }

    /**
     * Clears the user ID from SharedPreferences, typically called on user logout.
     */
    fun clearUserId() {
        with(preferences.edit()) {
            remove(USER_ID_KEY)
            apply()
        }
        Log.d("PreferencesManager", "Cleared userId")
    }

    /**
     * Sets whether the app should prompt for login on the next launch.
     */
    fun setPromptLogin(prompt: Boolean) {
        with(preferences.edit()) {
            putBoolean(PROMPT_LOGIN_KEY, prompt)
            apply()
        }
        Log.d("PreferencesManager", "Set prompt login to: $prompt")
    }

    /**
     * Updates the timestamp of the last login.
     */
    fun updateLastLoginTimestamp() {
        val timestamp = Calendar.getInstance().timeInMillis
        with(preferences.edit()) {
            putLong(LAST_LOGIN_TIMESTAMP_KEY, timestamp)
            apply()
        }
        Log.d("PreferencesManager", "Updated last login timestamp: $timestamp")
    }
}
