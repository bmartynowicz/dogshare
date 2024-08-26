package com.dogshare.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dogshare.R

@Composable
fun WelcomeScreen(username: String) {
    // Define colors based on the logo color scheme
    val logoOutlineColor = Color(0xFF16202A) // Logo outline color
    val backgroundColor = Color(0xFFE1DDCC) // Background color

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo), // Assuming you have 'logo' drawable
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome, $username!",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6200EE) // Temporary purple color; replace as needed
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { /* TODO: Implement navigation or actions */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = logoOutlineColor,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text("Continue")
            }
        }
    }
}
