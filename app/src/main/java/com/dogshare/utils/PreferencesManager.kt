package com.dogshare.utils

import android.content.Context
import android.content.SharedPreferences

object PreferencesManager {
    private const val PREFS_NAME = "user_settings"
    private lateinit var prefs: SharedPreferences

    /**
     * Initializes SharedPreferences instance for this application. Should be called once
     * in Application.onCreate() or similar.
     */
    fun initialize(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun ensureInitialized() {
        if (!::prefs.isInitialized) {
            throw IllegalStateException("PreferencesManager not initialized. Call initialize() first.")
        }
    }

    fun getBooleanPreference(key: String, defaultValue: Boolean): Boolean {
        ensureInitialized()
        return prefs.getBoolean(key, defaultValue)
    }

    fun setBooleanPreference(key: String, value: Boolean) {
        ensureInitialized()
        prefs.edit().putBoolean(key, value).apply()
    }
}
