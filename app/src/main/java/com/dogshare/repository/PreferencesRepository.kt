package com.dogshare.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.firebase.firestore.FirebaseFirestore

class PreferencesRepository(context: Context) {

    companion object {
        private const val PREFS_NAME = "app_preferences"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_DARK_MODE_ENABLED = "dark_mode_enabled"
        private const val KEY_ACCOUNT_PRIVACY_ENABLED = "account_privacy_enabled"
        private const val KEY_USER_ID = "user_id"
        private const val PROMPT_LOGIN_KEY = "prompt_login"
        private const val LAST_LOGIN_TIMESTAMP_KEY = "last_login_timestamp"
        private const val KEY_FIRST_LOGIN_COMPLETED = "isFirstLoginCompleted"
        private const val KEY_BREED_PREFERENCE = "breed_preference"
        private const val KEY_LOCATION_RADIUS = "location_radius"
        private const val KEY_AVAILABILITY = "availability"
        private const val KEY_ACTIVITY_LEVEL = "activity_level"

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

    private fun getFirstLoginCompletedKey(): String {
        val userId = getUserId()
        return "$KEY_FIRST_LOGIN_COMPLETED$userId"
    }

    fun isFirstLoginCompleted(): Boolean {
        val key = getFirstLoginCompletedKey()
        val isCompleted = sharedPreferences.getBoolean(key, false)
        Log.d("PreferencesRepository", "isFirstLoginCompleted(): key=$key, isCompleted=$isCompleted")
        return isCompleted
    }

    fun setFirstLoginCompleted() {
        val key = getFirstLoginCompletedKey()
        sharedPreferences.edit().putBoolean(key, true).apply()
        Log.d("PreferencesRepository", "First login completed set to true for key: $key")
    }

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

    fun getBreedPreference(): String? = sharedPreferences.getString(KEY_BREED_PREFERENCE, null)
    fun getLocationRadius(): Int = sharedPreferences.getInt(KEY_LOCATION_RADIUS, 0)
    fun getAvailability(): String? = sharedPreferences.getString(KEY_AVAILABILITY, null)
    fun getActivityLevel(): String? = sharedPreferences.getString(KEY_ACTIVITY_LEVEL, null)

    fun setBreedPreference(breed: String) {
        sharedPreferences.edit().putString(KEY_BREED_PREFERENCE, breed).apply()
    }

    fun setLocationRadius(radius: Int) {
        sharedPreferences.edit().putInt(KEY_LOCATION_RADIUS, radius).apply()
    }

    fun setAvailability(availability: String) {
        sharedPreferences.edit().putString(KEY_AVAILABILITY, availability).apply()
    }

    fun setActivityLevel(activityLevel: String) {
        sharedPreferences.edit().putString(KEY_ACTIVITY_LEVEL, activityLevel).apply()
    }

    fun syncPreferencesToCloud(userId: String) {
        val userSettings = mapOf(
            "breed_preference" to getBreedPreference(),
            "location_radius" to getLocationRadius(),
            "availability" to getAvailability(),
            "activity_level" to getActivityLevel()
            // Add other preferences as needed
        )

        FirebaseFirestore.getInstance().collection("userPreferences")
            .document(userId)
            .set(userSettings)
            .addOnSuccessListener { Log.d("PreferencesRepository", "Preferences synced successfully!") }
            .addOnFailureListener { e -> Log.e("PreferencesRepository", "Error syncing preferences", e) }
    }

    // Clear additional preferences when needed
    fun clearAdditionalPreferences() {
        sharedPreferences.edit().remove(KEY_BREED_PREFERENCE)
            .remove(KEY_LOCATION_RADIUS)
            .remove(KEY_AVAILABILITY)
            .remove(KEY_ACTIVITY_LEVEL)
            .apply()
    }



}
