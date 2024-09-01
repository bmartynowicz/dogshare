package com.dogshare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

class ProfileUpdateViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // State for loading and updating the UI based on data operations
    var operationStatus = mutableStateOf("")
    var userProfile = mutableStateOf(UserProfile("", "", "", "", "", ""))

    fun loadUserProfile(userId: String) {
        db.collection("profiles").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val profile = document.toObject(UserProfile::class.java)
                    userProfile.value = profile ?: UserProfile("", "", "", "", "", "")
                }
            }
            .addOnFailureListener {
                operationStatus.value = "Failed to load profile: ${it.message}"
            }
    }

    fun updateProfile(userId: String, data: Map<String, Any>, onSuccess: () -> Unit, onError: (String) -> Unit) {
        db.collection("profiles").document(userId)
            .set(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.localizedMessage ?: "Unknown error occurred") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileUpdateScreen(
    userId: String,
    navController: NavController,
    viewModel: ProfileUpdateViewModel = viewModel()
) {
    // This will cause the screen to reload user profile on composition or re-compositions triggered by lifecycle events.
    LaunchedEffect(key1 = userId) {
        viewModel.loadUserProfile(userId)
    }

    // Accessing state directly
    val userProfile = viewModel.userProfile.value
    val statusMessage = viewModel.operationStatus.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = userProfile.email,
                onValueChange = { viewModel.userProfile.value = userProfile.copy(email = it) },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            // Additional fields are added similarly...

            Button(
                onClick = {
                    viewModel.updateProfile(userId, userProfile.toMap(), onSuccess = {
                        viewModel.operationStatus.value = "Profile Updated Successfully"
                    }, onError = { error ->
                        viewModel.operationStatus.value = error
                    })
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update")
            }

            // Showing status message with a condition
            if (statusMessage.isNotEmpty()) {
                Text(text = statusMessage, color = MaterialTheme.colorScheme.error)
                // Clear the status message after showing it
                LaunchedEffect(statusMessage) {
                    viewModel.operationStatus.value = ""
                }
            }
        }
    }
}

// Example of a helper function inside the UserProfile data class to convert to Map
data class UserProfile(
    var email: String,
    var petType: String,
    var petSize: String,
    var livingCondition: String,
    var activityLevel: String,
    var travelDistance: String
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "email" to email,
            "petType" to petType,
            "petSize" to petSize,
            "livingCondition" to livingCondition,
            "activityLevel" to activityLevel,
            "travelDistance" to travelDistance
        )
    }
}
