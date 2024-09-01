package com.dogshare.ui.screens

import android.util.Log
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.dogshare.navigation.NavigationRoutes
import com.dogshare.ui.components.BottomNavigationBar

@Composable
fun LandingPageScreen(
    userId: String?,
    navController: NavController,
    onLogout: () -> Unit
) {
    val context = LocalContext.current

    userId?.let {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController = navController, userId = it)
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Welcome to the Landing Page, User ID: $userId")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        Log.d("TestButton", "Navigating to Settings screen")
                        navController.navigate(NavigationRoutes.Settings.route) {
                            Log.d("TestButton", "Navigation to Settings initiated")
                            launchSingleTop = true  // Ensure that only one instance of the destination is on the stack
                            restoreState = true  // Restore state when reselecting a previously selected item
                        }
                    }
                ) {
                    Text("Go to Settings")
                }

                Button(onClick = {
                    logoutUser(context, navController)
                    onLogout()
                }) {
                    Text(text = "Logout")
                }
            }
        }
    }
}

private fun logoutUser(context: Context, navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    auth.signOut()  // Sign out from Firebase

    // Clear SharedPreferences (if you store login info or other user data)
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()

    // Navigate back to the login screen
    navController.navigate(NavigationRoutes.Login.route) {
        popUpTo(NavigationRoutes.LandingPage.route) { inclusive = true }
    }
}
