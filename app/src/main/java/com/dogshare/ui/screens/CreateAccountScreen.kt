package com.dogshare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dogshare.repository.PreferencesRepository
import com.dogshare.viewmodels.CreateAccountViewModel
import org.koin.androidx.compose.getKoin
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateAccountScreen(
    navController: NavController,
    onLoginInstead: () -> Unit,
    viewModel: CreateAccountViewModel = koinViewModel()  // Using koinViewModel for Koin dependency injection
) {
    val email by remember { viewModel.email }
    val password by remember { viewModel.password }
    val confirmPassword by remember { viewModel.confirmPassword }
    val isLoading by viewModel.isLoading.collectAsState()
    val createAccountState by viewModel.createAccountState.collectAsState()
    val preferencesRepository: PreferencesRepository = getKoin().get()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { viewModel.updateEmail(it) },
                        label = { Text("Email") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Email
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { viewModel.updatePassword(it) },
                        label = { Text("Password") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Password
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { viewModel.updateConfirmPassword(it) },
                        label = { Text("Confirm Password") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Password
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.createAccount()
                            preferencesRepository.setFirstLoginCompleted()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Create Account")
                    }

                    if (createAccountState.isNotEmpty()) {
                        Text(
                            text = createAccountState,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (createAccountState.contains("successfully", ignoreCase = true)) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.error
                            },
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = onLoginInstead) {
                        Text("Already have an account? Log in")
                    }
                }
            }
        }
    }
}