package com.dogshare.viewmodels

import android.content.Context
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
        sharedPreferences.edit().clear().apply() // Clear preferences
        // You can also clean up other cached files or temporary data here
    }
}
