package com.dogshare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    username: String,
    navController: NavController,
    onStartQuestionnaire: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Welcome, $username") }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("Welcome to DogShare!", fontSize = 32.sp, modifier = Modifier.padding(bottom = 8.dp))
                Text(
                    "We're here to help alleviate some traditional hardships of dog ownership such as residential accommodations, travel schedules, or job requirements.",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    "By subscribing, you get access to premium features like vet insurance for any pet you match with, for the duration of your pet ownership.",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Button(
                    onClick = {
                        // Navigate to the questionnaire or start the questionnaire logic
                        onStartQuestionnaire()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tell Us About Your Needs")
                }
            }
        }
    )
}

