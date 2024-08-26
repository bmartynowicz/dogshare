package com.dogshare
import LoginScreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.dogshare.ui.screens.*
import com.dogshare.ui.theme.DogShareTheme
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogShareTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf("login") }
    var username by remember { mutableStateOf("") }

    when (currentScreen) {
        "login" -> LoginScreen(
            onForgotPassword = {
                currentScreen = "forgotPassword"
            },
            onCreateAccount = {
                currentScreen = "createAccount"
            },
            onLoginSuccess = { userEmail ->
                username = userEmail
                currentScreen = "welcome"
            },
            onLoginFailed = { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        )
        "forgotPassword" -> ForgotPasswordScreen(
            onPasswordResetSent = {
                Toast.makeText(context, "Password reset email sent.", Toast.LENGTH_LONG).show()
                currentScreen = "login"
            },
            onError = { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        )
        "createAccount" -> CreateAccountScreen(
            onAccountCreated = {
                Toast.makeText(context, "Account created successfully.", Toast.LENGTH_LONG).show()
                currentScreen = "login"
            },
            onError = { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        )
        "welcome" -> WelcomeScreen(username)
    }
}
