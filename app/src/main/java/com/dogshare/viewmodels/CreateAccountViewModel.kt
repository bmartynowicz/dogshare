package com.dogshare.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dogshare.repository.PreferencesRepository
import com.dogshare.services.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateAccountViewModel(
    private val authService: AuthService,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    var email = mutableStateOf("")
        private set
    var password = mutableStateOf("")
        private set
    var confirmPassword = mutableStateOf("")
        private set

    private val _createAccountState = MutableStateFlow("")
    val createAccountState: StateFlow<String> = _createAccountState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword.value = newConfirmPassword
    }

    fun createAccount() {
        if (password.value != confirmPassword.value) {
            _createAccountState.value = "Passwords do not match"
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            val result = authService.createAccount(email.value, password.value)
            _isLoading.value = false
            _createAccountState.value = result.fold(
                onSuccess = { "Account created successfully!" },
                onFailure = { it.message ?: "An error occurred during account creation." }
            )
        }
    }
}