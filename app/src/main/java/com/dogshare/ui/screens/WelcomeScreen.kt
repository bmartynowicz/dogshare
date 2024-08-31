package com.dogshare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(username: String) {
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
                Text("Welcome to DogShare!", fontSize = 32.sp, modifier = Modifier.padding(16.dp))
                Text("Let's start exploring.", fontSize = 24.sp, modifier = Modifier.padding(16.dp))
                Button(onClick = { /* Start Exploring */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Get Started")
                }
            }
        }
    )
}
