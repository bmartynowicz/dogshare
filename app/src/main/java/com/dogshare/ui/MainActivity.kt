package com.dogshare.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.dogshare.navigation.AppNavigation
import com.dogshare.ui.theme.DogShareTheme
import com.dogshare.viewmodels.LoginViewModel
import org.koin.androidx.compose.getViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DogShareTheme {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent() {
    val loginViewModel: LoginViewModel = getViewModel()
    val navController = rememberNavController()
    val isLoading by loginViewModel.isLoading.collectAsState()
    val navigationCommand by loginViewModel.navigationCommand.collectAsState()

    LaunchedEffect(navigationCommand) {
        navigationCommand?.let { navController.navigate(it) }
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        AppNavigation(navController = navController, startDestination = navigationCommand ?: "Login")
    }
}