package com.dogshare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dogshare.ui.components.BottomNavigationBar
import com.dogshare.ui.components.SettingItemToggle
import com.dogshare.viewmodels.SettingsViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.platform.LocalContext

@Composable
fun SettingsScreen(
    userId: String,
    navController: NavController
) {
    // Inject the SettingsViewModel using Koin
    val viewModel: SettingsViewModel = koinViewModel()

    // Get the current context
    val context = LocalContext.current

    // Load preferences when the screen is composed
    LaunchedEffect(context) {
        viewModel.loadPreferences() // Ensure preferences are loaded on composition
    }

    // Collect state values from the ViewModel
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val darkModeEnabled by viewModel.darkModeEnabled.collectAsState()
    val accountPrivacyEnabled by viewModel.accountPrivacyEnabled.collectAsState()

    // Scaffold for the Settings screen layout
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, userId = userId) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title of the Settings screen
            Text(text = "Settings", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            // Toggle for Notifications setting
            SettingItemToggle(
                settingKey = "notifications",
                settingLabel = "Enable Notifications",
                isChecked = notificationsEnabled,
                onSettingChanged = { key, isEnabled ->
                    viewModel.updatePreference(key, isEnabled)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Toggle for Dark Mode setting
            SettingItemToggle(
                settingKey = "dark_mode",
                settingLabel = "Enable Dark Mode",
                isChecked = darkModeEnabled,
                onSettingChanged = { key, isEnabled ->
                    viewModel.updatePreference(key, isEnabled)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Toggle for Account Privacy setting
            SettingItemToggle(
                settingKey = "account_privacy",
                settingLabel = "Private Account",
                isChecked = accountPrivacyEnabled,
                onSettingChanged = { key, isEnabled ->
                    viewModel.updatePreference(key, isEnabled)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
