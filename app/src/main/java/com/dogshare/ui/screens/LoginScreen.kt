package com.dogshare.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.dogshare.R
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onForgotPassword: () -> Unit,
    onCreateAccount: () -> Unit,
    onLoginSuccess: (String) -> Unit,
    onLoginFailed: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var loginErrorMessage by remember { mutableStateOf("") }
    val emailFocusRequester = FocusRequester()
    val passwordFocusRequester = FocusRequester()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.dog_image),
            contentDescription = "Background Image",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailFocusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() })
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFocusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    performLogin(auth, email.text, password.text, onLoginSuccess, onLoginFailed)
                })
            )
            if (loginErrorMessage.isNotEmpty()) {
                Text(
                    text = loginErrorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(4.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    performLogin(auth, email.text, password.text, onLoginSuccess, onLoginFailed)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log In")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onForgotPassword) {
                Text("Forgot Password?")
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onCreateAccount) {
                Text("Create Account")
            }
        }
    }
}

fun performLogin(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onLoginSuccess: (String) -> Unit,
    onLoginFailed: (String) -> Unit
) {
    if (email.isNotBlank() && password.isNotBlank()) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onLoginSuccess(email)
                } else {
                    onLoginFailed(task.exception?.message ?: "Authentication failed")
                }
            }
    } else {
        onLoginFailed("Please enter both email and password.")
    }
}
