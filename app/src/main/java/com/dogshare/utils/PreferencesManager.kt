package com.dogshare.utils

import android.content.Context
import android.content.SharedPreferences

object PreferencesManager {
    private const val PREFS_NAME = "user_settings"

    fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getBooleanPreference(context: Context, key: String, defaultValue: Boolean): Boolean {
        return getSharedPreferences(context).getBoolean(key, defaultValue)
    }

    fun setBooleanPreference(context: Context, key: String, value: Boolean) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
}
