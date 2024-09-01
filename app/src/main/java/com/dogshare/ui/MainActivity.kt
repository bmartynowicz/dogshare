package com.dogshare.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.dogshare.navigation.AppNavigation
import com.dogshare.navigation.NavigationRoutes
import com.dogshare.ui.theme.DogShareTheme
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogShareTheme {
                DogShareApp()
            }
        }
    }
}

@Composable
fun DogShareApp() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val lastLoginTimestamp = sharedPreferences.getLong("last_login_timestamp", 0L)
        val currentTime = System.currentTimeMillis()

        // Calculate the time difference in days
        val daysSinceLastLogin = TimeUnit.MILLISECONDS.toDays(currentTime - lastLoginTimestamp)

        if (auth.currentUser != null && daysSinceLastLogin <= 30) {
            // User is signed in and last login was within 30 days, navigate to LandingPage
            navController.navigate(NavigationRoutes.LandingPage.createRoute(auth.currentUser!!.uid)) {
                popUpTo(NavigationRoutes.Login.route) { inclusive = true }
            }
        } else {
            // User is not signed in or it has been more than 30 days
            // Sign the user out and navigate to the login screen
            auth.signOut()
            navController.navigate(NavigationRoutes.Login.route) {
                popUpTo(NavigationRoutes.LandingPage.route) { inclusive = true }
            }
        }
    }

    AppNavigation(navController = navController)
}
