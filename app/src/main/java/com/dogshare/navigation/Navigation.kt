package com.dogshare.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dogshare.ui.screens.*
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) "Landing" else "Login"

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(
            route = "Login",
            enterTransition = {
                fadeIn(animationSpec = tween(700)) + slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth })
            },
            exitTransition = {
                fadeOut(animationSpec = tween(700)) + slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth })
            }
        ) {
            LoginScreen(
                onForgotPassword = { /* Handle Forgot Password */ },
                onCreateAccount = { /* Handle Create Account */ },
                onLoginSuccess = { userEmail ->
                    navController.navigate("Landing") {
                        popUpTo("Login") { inclusive = true }
                    }
                },
                onLoginFailed = { errorMessage ->
                    // Handle login failure
                }
            )
        }
        composable(
            route = "Landing",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(300))
            }
        ) {
            LandingPage(username = auth.currentUser?.email ?: "", navController = navController)
        }
        composable(
            route = "Matches",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(300))
            }
        ) {
            MatchesScreen(navController = navController) // Ensure this is correctly imported
        }
        composable(
            route = "Settings",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth }, animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }, animationSpec = tween(300))
            }
        ) {
            SettingsScreen(navController = navController)
        }
        composable("Profile") {
            ProfilePage(
                username = auth.currentUser?.email ?: "",
                onPreferencesUpdated = {
                    navController.navigate("Landing") {
                        popUpTo("Landing") { inclusive = true }
                    }
                },
                onLogout = {
                    navController.navigate("Login") {
                        popUpTo("Landing") { inclusive = true } // Clears the back stack so user can't go back to the previous screen
                    }
                }
            )
        }
    }
}

@Composable
fun SettingsScreen(navController: NavHostController) {

}
