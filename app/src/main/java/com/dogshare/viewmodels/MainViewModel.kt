package com.dogshare.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class MainViewModel(private val auth: FirebaseAuth) : ViewModel() {

    fun checkUserStatus(context: Context) {
        viewModelScope.launch {
            val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
            val lastLoginTimestamp = sharedPreferences.getLong("last_login_timestamp", 0L)
            val currentTime = System.currentTimeMillis()
            val daysSinceLastLogin = TimeUnit.MILLISECONDS.toDays(currentTime - lastLoginTimestamp)

            if (auth.currentUser != null && daysSinceLastLogin <= 30) {
                // TODO: Implement logic for handling active user
            } else {
                // TODO: Implement user sign-out and redirection logic
            }
        }
    }
}
