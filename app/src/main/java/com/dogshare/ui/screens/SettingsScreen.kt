package com.dogshare.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dogshare.ui.components.BottomNavigationBar
import com.dogshare.ui.components.SettingItemToggle
import com.dogshare.ui.utils.checkUserPreferences
import com.dogshare.utils.PreferencesManager

@Composable
fun SettingsScreen(
    context: Context,
    userId: String,
    navController: NavController,
    onSettingChanged: (String, Boolean) -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(false) }
    var darkModeEnabled by remember { mutableStateOf(false) }
    var accountPrivacyEnabled by remember { mutableStateOf(false) }

    Log.d("Initializing SettingsScreen", "userId: $userId")

    LaunchedEffect(userId) {
        if (userId.isNotBlank()) {
            checkUserPreferences(userId) { hasPreferences ->
                if (hasPreferences) {
                    notificationsEnabled = PreferencesManager.getBooleanPreference(context, "notifications", false)
                    darkModeEnabled = PreferencesManager.getBooleanPreference(context, "dark_mode", false)
                    accountPrivacyEnabled = PreferencesManager.getBooleanPreference(context, "account_privacy", false)
                }
            }
        } else {
            Log.d("SettingsScreen", "Invalid userId: $userId")
        }
    }

    Log.d("SettingsScreen", "Loaded settings for userId: $userId")

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, userId = userId)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(text = "Settings", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            SettingItemToggle(
                settingKey = "notifications",
                settingLabel = "Enable Notifications",
                isChecked = notificationsEnabled,
                onSettingChanged = { key, isEnabled ->
                    notificationsEnabled = isEnabled
                    PreferencesManager.setBooleanPreference(context, key, isEnabled)
                    onSettingChanged(key, isEnabled)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingItemToggle(
                settingKey = "dark_mode",
                settingLabel = "Enable Dark Mode",
                isChecked = darkModeEnabled,
                onSettingChanged = { key, isEnabled ->
                    darkModeEnabled = isEnabled
                    PreferencesManager.setBooleanPreference(context, key, isEnabled)
                    onSettingChanged(key, isEnabled)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingItemToggle(
                settingKey = "account_privacy",
                settingLabel = "Private Account",
                isChecked = accountPrivacyEnabled,
                onSettingChanged = { key, isEnabled ->
                    accountPrivacyEnabled = isEnabled
                    PreferencesManager.setBooleanPreference(context, key, isEnabled)
                    onSettingChanged(key, isEnabled)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

