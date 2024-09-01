package com.dogshare.repository

import android.content.Context
import android.content.SharedPreferences

class PreferencesRepository(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "user_settings"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications"
        private const val KEY_DARK_MODE_ENABLED = "dark_mode"
        private const val KEY_ACCOUNT_PRIVACY_ENABLED = "account_privacy"
    }

    // Load preferences
    fun getNotificationsEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_NOTIFICATIONS_ENABLED, false)
    }

    fun getDarkModeEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_DARK_MODE_ENABLED, false)
    }

    fun getAccountPrivacyEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_ACCOUNT_PRIVACY_ENABLED, false)
    }

    // Save preferences
    fun setNotificationsEnabled(isEnabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, isEnabled).apply()
    }

    fun setDarkModeEnabled(isEnabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_DARK_MODE_ENABLED, isEnabled).apply()
    }

    fun setAccountPrivacyEnabled(isEnabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_ACCOUNT_PRIVACY_ENABLED, isEnabled).apply()
    }
}
