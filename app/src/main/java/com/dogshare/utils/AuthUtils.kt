package com.dogshare.utils

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object AuthUtils {
    private const val PREFS_NAME = "com.dogshare.prefs"
    private const val KEY_IS_LOGGED_IN = "isLoggedIn"

    // Lazy initialization to ensure context is available
    private lateinit var prefs: SharedPreferences

    // StateFlow to hold the login state
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    /**
     * Initializes the SharedPreferences and StateFlow based on the saved value.
     * @param context the context used to access SharedPreferences.
     */
    fun initialize(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        _isLoggedIn.value = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /**
     * Checks if the user is currently logged in.
     * @return true if the user is logged in, false otherwise.
     */
    fun isUserLoggedIn(): Boolean {
        return _isLoggedIn.value
    }

    /**
     * Sets the user's login status.
     * @param isLoggedIn true if the user is logged in, false otherwise.
     */
    fun setUserLoggedIn(isLoggedIn: Boolean) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
        _isLoggedIn.value = isLoggedIn
    }
}
