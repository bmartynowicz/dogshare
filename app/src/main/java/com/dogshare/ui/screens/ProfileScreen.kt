package com.dogshare.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dogshare.viewmodels.LogoutViewModel
import com.dogshare.viewmodels.ProfileViewModel
import org.koin.androidx.compose.koinViewModel
import com.dogshare.ui.components.BottomNavigationBar

@Composable
fun ProfileScreen(
    userId: String,
    navController: NavController,
    viewModel: ProfileViewModel = koinViewModel(),
    logoutViewModel: LogoutViewModel = koinViewModel()
) {
    // State variables for profile fields
    val username by viewModel.username.collectAsState()
    val email by viewModel.email.collectAsState()
    val breedPreference by viewModel.breedPreference.collectAsState()
    val sizePreference by viewModel.sizePreference.collectAsState()
    val availability by viewModel.availability.collectAsState()
    val travelDistance by viewModel.travelDistance.collectAsState()

    // State variables for settings
    val notificationEnabled by viewModel.notificationEnabled.collectAsState()
    val darkModeEnabled by viewModel.darkModeEnabled.collectAsState()
    val isDeletingAccount by viewModel.isDeletingAccount.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(userId) {
        viewModel.fetchProfile(userId)
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, userId = userId) }
    ) { innerPadding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Profile", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                // Profile Information Section
                ProfileInformationSection(
                    username = username,
                    email = email,
                    breedPreference = breedPreference,
                    sizePreference = sizePreference,
                    availability = availability,
                    travelDistance = travelDistance,
                    onSaveProfile = { viewModel.saveProfile(userId) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Settings Section
                SettingsSection(
                    notificationEnabled = notificationEnabled,
                    onNotificationToggle = { viewModel.updateNotificationEnabled(it) },
                    darkModeEnabled = darkModeEnabled,
                    onDarkModeToggle = { viewModel.updateDarkModeEnabled(it) },
                    onChangePassword = { /* Navigate to Change Password Screen */ },
                    onDeleteAccount = { viewModel.deleteAccount(userId) },
                    isDeletingAccount = isDeletingAccount
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Logout Button
                Button(
                    onClick = {
                        logoutViewModel.logout {
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }  // Clear back stack
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                ) {
                    Text("Log Out")
                }
            }
        }
    }
}


@Composable
fun ProfileInformationSection(
    username: String,
    email: String,
    breedPreference: String,
    sizePreference: String,
    availability: String,
    travelDistance: String,
    onSaveProfile: () -> Unit
) {
    Text(text = "Profile Information", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))

    Text(text = "Username: $username", style = MaterialTheme.typography.bodyLarge)
    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = email,
        onValueChange = { /* Optionally handle email change */ },
        label = { Text("Email") },
        singleLine = true,
        enabled = false, // Email should be non-editable in profile screen
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        value = breedPreference,
        onValueChange = { /* Optionally handle breed preference change */ },
        label = { Text("Breed Preference") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        value = sizePreference,
        onValueChange = { /* Optionally handle size preference change */ },
        label = { Text("Size Preference") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        value = availability,
        onValueChange = { /* Optionally handle availability change */ },
        label = { Text("Availability") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        value = travelDistance,
        onValueChange = { /* Optionally handle travel distance change */ },
        label = { Text("Willing to travel (in miles)") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = onSaveProfile,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Save Profile")
    }
}


@Composable
fun SettingsSection(
    notificationEnabled: Boolean,
    onNotificationToggle: (Boolean) -> Unit,
    darkModeEnabled: Boolean,
    onDarkModeToggle: (Boolean) -> Unit,
    onChangePassword: () -> Unit,
    onDeleteAccount: () -> Unit,
    isDeletingAccount: Boolean
) {
    Text(text = "Settings", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))

    // Notification Preference
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Enable Notifications", modifier = Modifier.weight(1f))
        Switch(
            checked = notificationEnabled,
            onCheckedChange = onNotificationToggle
        )
    }

    // Dark Mode Preference
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Dark Mode", modifier = Modifier.weight(1f))
        Switch(
            checked = darkModeEnabled,
            onCheckedChange = onDarkModeToggle
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Change Password Button
    Button(
        onClick = onChangePassword,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Change Password")
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Delete Account Button
    Button(
        onClick = onDeleteAccount,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
    ) {
        if (isDeletingAccount) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
        } else {
            Text("Delete Account")
        }
    }
}


