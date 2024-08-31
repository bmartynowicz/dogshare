package com.dogshare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun LoginScreen(
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit,
    onLoginSuccess: (String) -> Unit,
    onLoginFailed: (String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login Screen")

        Spacer(modifier = Modifier.height(16.dp))

        // Username TextField
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password TextField
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation() // Hide the password input
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Handle login logic
            if (username.isNotBlank() && password.isNotBlank()) {
                onLoginSuccess("userId123")  // Replace with actual login logic
            } else {
                onLoginFailed("Username or password cannot be empty")
            }
        }) {
            Text(text = "Log In")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onForgotPassword) {
            Text(text = "Forgot Password")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onCreateAccount) {
            Text(text = "Create Account")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { onLoginFailed("Login failed") }) {
            Text(text = "Login Failed")
        }
    }
}
