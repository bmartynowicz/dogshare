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

    // Inject PreferencesRepository
    private val preferencesRepository: PreferencesRepository by inject(PreferencesRepository::class.java)

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            // Sign out from Firebase Authentication
            auth.signOut()

            // Clear user-related preferences using PreferencesRepository
            preferencesRepository.clearUserId()
            preferencesRepository.setPromptLogin(true)

            // Perform additional cleanup if necessary

            // Call the success callback to indicate logout is complete
            onLogoutSuccess()
        }
    }
}
