package com.dogshare.ui.screens

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
    logoutViewModel: LogoutViewModel = koinViewModel() // Inject LogoutViewModel
) {
    // Observe the states from the ProfileViewModel
    val email by viewModel.email.collectAsState()
    val petType by viewModel.petType.collectAsState()
    val petSize by viewModel.petSize.collectAsState()
    val livingCondition by viewModel.livingCondition.collectAsState()
    val activityLevel by viewModel.activityLevel.collectAsState()
    val travelDistance by viewModel.travelDistance.collectAsState()
    val saveProfileState by viewModel.saveProfileState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Effect to fetch profile data whenever userId changes or re-fetch on re-composition
    LaunchedEffect(userId) {
        viewModel.fetchProfile(userId)
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, userId = userId) }
    ) { innerPadding ->
        if (isLoading) {
            // Show loading indicator
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Profile", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.updateEmail(it) },
                    label = { Text("Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = petType,
                    onValueChange = { viewModel.updatePetType(it) },
                    label = { Text("Pet Type") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = petSize,
                    onValueChange = { viewModel.updatePetSize(it) },
                    label = { Text("Pet Size") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = livingCondition,
                    onValueChange = { viewModel.updateLivingCondition(it) },
                    label = { Text("Living Condition") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = activityLevel,
                    onValueChange = { viewModel.updateActivityLevel(it) },
                    label = { Text("Activity Level") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = travelDistance,
                    onValueChange = { viewModel.updateTravelDistance(it) },
                    label = { Text("Travel Distance") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.saveProfile(userId)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Profile")
                }

                if (saveProfileState.isNotEmpty()) {
                    Text(saveProfileState, color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        logoutViewModel.logout {
                            // Navigate to login screen after logout
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
