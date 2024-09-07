package com.dogshare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginFailedScreen(
    onRetryLogin: () -> Unit,
    onForgotPassword: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login Failed", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Please check your credentials and try again.")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRetryLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Retry Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onForgotPassword) {
            Text(text = "Forgot Password?")
        }
    }
}