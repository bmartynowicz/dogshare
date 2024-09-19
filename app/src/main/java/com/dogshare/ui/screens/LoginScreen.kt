package com.dogshare.ui.screens

import android.content.Context
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
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.compose.getKoin

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

    val preferencesRepository: PreferencesRepository = getKoin().get()

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
                    onLoginSuccess = { userId, needsOnboarding ->
                        isLoading = false
                        if (needsOnboarding) {
                            // Navigate to the onboarding screen
                            navController.navigate(NavigationRoutes.WelcomeScreen.createRoute(userId)) {
                                popUpTo(NavigationRoutes.Login.route) { inclusive = true }
                            }
                        } else {
                            // Navigate to the main landing page
                            navController.navigate(NavigationRoutes.LandingPage.createRoute(userId)) {
                                popUpTo(NavigationRoutes.Login.route) { inclusive = true }
                            }
                        }
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
    onLoginSuccess: (String, Boolean) -> Unit,
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

                        // Fetch the user's profile from Firestore
                        val userProfileRef = FirebaseFirestore.getInstance().collection("profiles").document(userId)
                        userProfileRef.get()
                            .addOnSuccessListener { document ->
                                val isQuestionnaireCompleted = document.getBoolean("isQuestionnaireCompleted") ?: false
                                Log.d("performLogin", "isQuestionnaireCompleted: $isQuestionnaireCompleted")

                                onLoginSuccess(userId, !isQuestionnaireCompleted)
                            }
                            .addOnFailureListener { e ->
                                Log.e("performLogin", "Error fetching user profile: ${e.message}")
                                onLoginFailed("Error fetching user profile")
                            }
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



fun markOnboardingComplete(context: Context) {
    val sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putBoolean("OnboardingCompleted", true)
        apply()
    }
}