package com.dogshare.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.dogshare.repository.PreferencesRepository
import com.google.firebase.auth.FirebaseAuth
import org.koin.java.KoinJavaComponent.inject
import androidx.navigation.NavController
import com.dogshare.navigation.NavigationRoutes

@Composable
fun LoginScreen(
    navController: NavController,
    onGoogleSignIn: () -> Unit,    // Google Sign-In callback
    onCreateAccount: () -> Unit,
    onLoginSuccess: (String) -> Unit,
    onLoginFailed: (String) -> Unit,
    onForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginErrorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val preferencesRepository: PreferencesRepository by inject(PreferencesRepository::class.java)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        loginErrorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Button(
            onClick = {
                isLoading = true
                performLogin(
                    email = email,
                    password = password,
                    preferencesRepository = preferencesRepository,
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

        // Google Sign-In Button
        Button(
            onClick = {
                onGoogleSignIn()   // Trigger Google Sign-In
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Sign in with Google")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            navController.navigate(NavigationRoutes.ForgotPassword.route)
        }) {
            Text("Forgot Password?")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onCreateAccount) {
            Text("Create Account")
        }
    }
}

private fun performLogin(
    email: String,
    password: String,
    preferencesRepository: PreferencesRepository,
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
                        preferencesRepository.setUserId(userId)
                        preferencesRepository.updateLastLoginTimestamp()
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