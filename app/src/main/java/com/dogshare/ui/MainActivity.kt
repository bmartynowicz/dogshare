package com.dogshare.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.dogshare.navigation.AppNavigation
import com.dogshare.navigation.NavigationRoutes
import com.dogshare.ui.theme.DogShareTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)  // Ensure you're calling the superclass's onCreate method
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
    val context = LocalContext.current

    // State to track when the login check is completed
    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = true) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        startDestination = if (firebaseUser == null) {
            NavigationRoutes.Login.route
        } else {
            NavigationRoutes.LandingPage.createRoute(firebaseUser.uid)
        }
    }

    // Ensure that the navigation setup only happens once the start destination is determined
    startDestination?.let {
        AppNavigation(navController = navController)
    }
}
