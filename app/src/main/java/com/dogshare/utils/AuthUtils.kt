package com.dogshare.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


object AuthUtils {
    private const val PREFS_NAME = "com.dogshare.prefs"
    private const val KEY_IS_LOGGED_IN = "isLoggedIn"
    private const val TAG = "AuthUtils"

    private lateinit var prefs: SharedPreferences
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun initialize(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        updateLoginState(firebaseAuth.currentUser != null)

        // Listen to Firebase Authentication state changes
        firebaseAuth.addAuthStateListener { firebaseAuth ->
            val userLoggedIn = firebaseAuth.currentUser != null
            updateLoginState(userLoggedIn)
        }
    }

    private fun updateLoginState(isLoggedIn: Boolean) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
        _isLoggedIn.value = isLoggedIn
        Log.d(TAG, "AuthUtils updated: User logged in status set to $isLoggedIn")
    }

    fun isUserLoggedIn(): Boolean {
        return _isLoggedIn.value
    }

    fun setUserLoggedIn(isLoggedIn: Boolean) {
        if (isLoggedIn) {
            // Manage Firebase sign-in if needed
        } else {
            firebaseAuth.signOut()  // Sign out from Firebase
        }
        updateLoginState(isLoggedIn)
    }
}
