package com.dogshare.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.dogshare.navigation.AppNavigation
import com.dogshare.ui.theme.DogShareTheme
import com.dogshare.viewmodels.MainViewModel
import org.koin.androidx.compose.getViewModel

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
    val context = LocalContext.current
    val viewModel: MainViewModel = getViewModel() // Injecting MainViewModel using Koin

    LaunchedEffect(key1 = true) {
        viewModel.checkUserStatus(context)
    }

    AppNavigation(navController = navController)
}
