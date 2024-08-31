package com.dogshare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.dogshare.navigation.AppNavigation
import com.dogshare.ui.theme.DogShareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogShareApp()
        }
    }
}

@Composable
fun DogShareApp() {
    DogShareTheme {
        val navController = rememberNavController()
        AppNavigation(navController = navController) // No need to pass isLoggedIn
    }
}
