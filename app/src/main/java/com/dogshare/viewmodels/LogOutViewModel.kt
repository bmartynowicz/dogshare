package com.dogshare.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dogshare.repository.PreferencesRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class LogoutViewModel(private val context: Context) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val preferencesRepository: PreferencesRepository by inject(PreferencesRepository::class.java)

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            auth.signOut()  // Firebase sign out
            preferencesRepository.clearUserId()
            preferencesRepository.setPromptLogin(true)

            // Clear app cache or additional session data
            clearAppCache()

            onLogoutSuccess()
        }
    }

    private fun clearAppCache() {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            // Remove specific keys that you want to clear
            .remove("session_token")
            .remove("temporary_data")
            // Do not remove keys like "isFirstLoginCompleted" or "userId" if you want to keep them
            .apply()
        Log.d("LogoutViewModel", "App cache cleared")
        // You can also clean up other cached files or temporary data here
    }
}
