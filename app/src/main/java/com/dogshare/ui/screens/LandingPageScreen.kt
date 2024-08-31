package com.dogshare.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.dogshare.navigation.NavigationRoutes

@Composable
fun LandingPageScreen(navController: NavController, userId: String?) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally  // Corrected this line
    ) {
        Text(text = "Welcome to the Landing Page, User ID: $userId")

        Spacer(modifier = Modifier.height(16.dp))

        // Logout button
        Button(onClick = {
            // Log out the user
            logoutUser(context, navController)
        }) {
            Text(text = "Logout")
        }
    }
}

fun logoutUser(context: Context, navController: NavController) {
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
