package com.dogshare.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(context: Context) : ViewModel() {

    private val PREFS_NAME = "com.dogshare.prefs"
    private val KEY_IS_LOGGED_IN = "isLoggedIn"
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        // Initialize login state on app launch
        updateLoginState(firebaseAuth.currentUser != null)
        listenForAuthStateChanges()
    }

    private fun listenForAuthStateChanges() {
        firebaseAuth.addAuthStateListener { auth ->
            val userLoggedIn = auth.currentUser != null
            updateLoginState(userLoggedIn)
        }
    }

    private fun updateLoginState(isLoggedIn: Boolean) {
        // Save the login state in SharedPreferences
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
        _isLoggedIn.value = isLoggedIn
    }

    fun updateLoginStatus(isLoggedIn: Boolean) {
        viewModelScope.launch {
            if (isLoggedIn) {
                // Add additional login logic here if necessary
                firebaseAuth.currentUser?.let {
                    updateLoginState(true)
                }
            } else {
                // Handle logout and clear shared preferences
                firebaseAuth.signOut()
                updateLoginState(false)
            }
        }
    }
}