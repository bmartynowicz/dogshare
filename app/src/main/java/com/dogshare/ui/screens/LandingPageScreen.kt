package com.dogshare.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dogshare.R
import com.dogshare.navigation.NavigationRoutes
import com.dogshare.ui.components.BottomNavigationBar
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LandingPageScreen(
    userId: String?,
    navController: NavController,
    onLogout: () -> Unit
) {
    val context = LocalContext.current

    // Log userId to debug navigation issues
    Log.d("LandingPageScreen", "User ID: $userId")

    userId?.let {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_round),
                        contentDescription = "App Icon",
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Dogshare",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            bottomBar = {
                BottomNavigationBar(navController = navController, userId = it)
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                SwipingScreen(userId = userId, navController = navController)
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

private fun logoutUser(context: Context, navController: NavController) {
    Log.d("LandingPageScreen", "Logging out user")

    val auth = FirebaseAuth.getInstance()
    auth.signOut()

    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()

    navController.navigate(NavigationRoutes.Login.route) {
        popUpTo(NavigationRoutes.LandingPage.route) { inclusive = true }
    }
}