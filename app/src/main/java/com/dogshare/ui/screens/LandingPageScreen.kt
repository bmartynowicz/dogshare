package com.dogshare.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.dogshare.navigation.NavigationRoutes
import com.dogshare.ui.components.BottomNavigationBar

@Composable
fun LandingPageScreen(
    userId: String?,
    navController: NavController,
    onLogout: () -> Unit
) {
    // Log userId to debug navigation issues
    Log.d("LandingPageScreen", "User ID: $userId")

    userId?.let {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController = navController, userId = it)
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                SwipingScreen(userId = userId)  // Embedded swiping screen within the LandingPageScreen
            }
        }
    } ?: run {
        // Redirect to login if userId is null
        Log.d("LandingPageScreen", "User ID is null, navigating to Login Screen")
        navController.navigate(NavigationRoutes.Login.route) {
            popUpTo(NavigationRoutes.LandingPage.route) { inclusive = true }
        }
    }
}
