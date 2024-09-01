package com.dogshare.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dogshare.utils.AuthUtils
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(context: Context) : ViewModel() {
    val isLoggedIn: StateFlow<Boolean> = AuthUtils.isLoggedIn

    init {
        AuthUtils.initialize(context)
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            // Ensures initialization logic if needed
        }
    }

    fun updateLoginStatus(isLoggedIn: Boolean) {
        viewModelScope.launch {
            AuthUtils.setUserLoggedIn(isLoggedIn)
        }
    }
}
