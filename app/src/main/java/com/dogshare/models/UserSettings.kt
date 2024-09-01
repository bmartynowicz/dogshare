package com.dogshare.models

data class UserSettings(
    val notificationsEnabled: Boolean = false,
    val darkModeEnabled: Boolean = false,
    val accountPrivacyEnabled: Boolean = false
)