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
fun LoginScreen(
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit,
    onLoginSuccess: (String) -> Unit,
    onLoginFailed: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginErrorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Get the current context
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineSmall)

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
            visualTransformation = PasswordVisualTransformation() // Hide the password input
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display an error message if login fails
        loginErrorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Log In button
        Button(
            onClick = {
                isLoading = true
                // Handle login logic
                performLogin(
                    email = email,
                    password = password,
                    context = context,  // Pass the context here
                    onLoginSuccess = { userId ->
                        isLoading = false
                        onLoginSuccess(userId)
                    },
                    onLoginFailed = { error ->
                        isLoading = false
                        loginErrorMessage = error
                        onLoginFailed(error)
                    }
                )
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
                Text(text = "Log In")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onForgotPassword) {
            Text(text = "Forgot Password?")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onCreateAccount) {
            Text(text = "Create Account")
        }
    }
}

private fun performLogin(
    email: String,
    password: String,
    context: Context,  // Accept the Context as a parameter
    onLoginSuccess: (String) -> Unit,
    onLoginFailed: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    if (email.isNotBlank() && password.isNotBlank()) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        // Save the current timestamp
                        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            putLong("last_login_timestamp", System.currentTimeMillis())
                            apply()
                        }
                        onLoginSuccess(userId)
                    } else {
                        onLoginFailed("Failed to retrieve user ID")
                    }
                } else {
                    onLoginFailed(task.exception?.message ?: "Authentication failed")
                }
            }
    } else {
        onLoginFailed("Email or password cannot be empty")
    }
}
