package com.dogshare.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainViewModel(private val auth: FirebaseAuth) : ViewModel() {

    fun checkUserStatus(context: Context, onStatusChecked: (isLoggedIn: Boolean) -> Unit) {
        viewModelScope.launch {
            val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
            val lastLoginTimestamp = sharedPreferences.getLong("last_login_timestamp", 0L)
            val currentTime = System.currentTimeMillis()
            val daysSinceLastLogin = TimeUnit.MILLISECONDS.toDays(currentTime - lastLoginTimestamp)

            if (auth.currentUser != null && daysSinceLastLogin <= 30) {
                // User is logged in and active within 30 days
                onStatusChecked(true)
            } else {
                // Session expired or user not logged in, clear session data
                auth.signOut()
                sharedPreferences.edit().clear().apply()
                onStatusChecked(false)
            }
        }
    }
}
