package com.dogshare.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dogshare.services.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.dogshare.repository.PreferencesRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class LoginViewModel(
    private val authService: AuthService,
    private val preferencesRepository: PreferencesRepository,
    private val application: Application
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _loginState = MutableStateFlow("")
    val loginState: StateFlow<String> = _loginState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _navigationCommand = MutableStateFlow<String?>(null)
    val navigationCommand: StateFlow<String?> = _navigationCommand

    init {
        checkAuthentication()
    }

    private fun checkAuthentication() {
        val userId = preferencesRepository.getUserId()
        if (userId != null && authService.isSignedIn()) {
            _navigationCommand.value = "LandingPage/$userId"
        } else {
            _navigationCommand.value = "Login"
        }
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun login() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = authService.signIn(_email.value, _password.value)
            _isLoading.value = false
            result.fold(
                onSuccess = {
                    preferencesRepository.setUserId(it)
                    _loginState.value = "Login successful!"
                    _navigationCommand.value = "LandingPage/$it"
                },
                onFailure = {
                    _loginState.value = it.message ?: "An error occurred during login."
                }
            )
        }
    }

    fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(application.getString(com.dogshare.R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(application, gso)
        viewModelScope.launch {
            try {
                val account = googleSignInClient.silentSignIn().await()
                handleGoogleSignInResult(account)
            } catch (e: Exception) {
                _loginState.value = "Google sign-in failed: ${e.localizedMessage}"
            }
        }
    }

    fun handleGoogleSignInResult(account: GoogleSignInAccount) {
        viewModelScope.launch {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            if (account.idToken != null) {
                authService.signInWithGoogle(account.idToken!!).fold(
                    onSuccess = {
                        preferencesRepository.setUserId(it)
                        _loginState.value = "Login successful!"
                        _navigationCommand.value = "LandingPage/$it"
                    },
                    onFailure = {
                        _loginState.value = "Google sign-in failed: ${it.localizedMessage}"
                    }
                )
            } else {
                _loginState.value = "Google sign-in failed: Missing ID Token"
            }
        }
    }

}
