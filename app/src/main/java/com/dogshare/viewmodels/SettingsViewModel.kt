package com.dogshare.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dogshare.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _notificationsEnabled = MutableStateFlow(false)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled

    private val _darkModeEnabled = MutableStateFlow(false)
    val darkModeEnabled: StateFlow<Boolean> = _darkModeEnabled

    private val _accountPrivacyEnabled = MutableStateFlow(false)
    val accountPrivacyEnabled: StateFlow<Boolean> = _accountPrivacyEnabled

    // Function to load preferences from PreferencesRepository
    fun loadPreferences() {
        viewModelScope.launch {
            _notificationsEnabled.value = preferencesRepository.getNotificationsEnabled()
            _darkModeEnabled.value = preferencesRepository.getDarkModeEnabled()
            _accountPrivacyEnabled.value = preferencesRepository.getAccountPrivacyEnabled()
        }
    }

    // Function to update preference and save it using PreferencesRepository
    fun updatePreference(key: String, value: Boolean) {
        viewModelScope.launch {
            when (key) {
                "notifications" -> {
                    _notificationsEnabled.value = value
                    preferencesRepository.setNotificationsEnabled(value)
                }
                "dark_mode" -> {
                    _darkModeEnabled.value = value
                    preferencesRepository.setDarkModeEnabled(value)
                }
                "account_privacy" -> {
                    _accountPrivacyEnabled.value = value
                    preferencesRepository.setAccountPrivacyEnabled(value)
                }
            }
        }
    }
}
