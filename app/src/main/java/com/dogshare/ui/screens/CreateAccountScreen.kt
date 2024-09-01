package com.dogshare.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CreateAccountScreen(
    onAccountCreated: (String) -> Unit,
    onAccountCreationFailed: (String) -> Unit,
    onLoginInstead: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Create Account", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        // Email TextField
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Confirm Password TextField
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display error message if account creation fails
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Create Account Button
        Button(
            onClick = {
                isLoading = true
                if (password == confirmPassword) {
                    createAccount(
                        email = email,
                        password = password,
                        context = context,
                        onAccountCreated = { userId ->
                            isLoading = false
                            onAccountCreated(userId)
                        },
                        onAccountCreationFailed = { error ->
                            isLoading = false
                            errorMessage = error
                            onAccountCreationFailed(error)
                        }
                    )
                } else {
                    isLoading = false
                    errorMessage = "Passwords do not match"
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(text = "Create Account")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onLoginInstead) {
            Text(text = "Already have an account? Log in")
        }
    }
}

private fun createAccount(
    email: String,
    password: String,
    context: Context,
    onAccountCreated: (String) -> Unit,
    onAccountCreationFailed: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    if (email.isNotBlank() && password.isNotBlank()) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        // Save the account creation time
                        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            putLong("last_login_timestamp", System.currentTimeMillis())
                            apply()
                        }
                        onAccountCreated(userId)
                    } else {
                        onAccountCreationFailed("Failed to retrieve user ID")
                    }
                } else {
                    onAccountCreationFailed(task.exception?.message ?: "Account creation failed")
                }
            }
    } else {
        onAccountCreationFailed("Email and password cannot be empty")
    }
}
