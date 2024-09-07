package com.dogshare.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dogshare.ui.screens.*

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {

    NavHost(navController = navController, startDestination = NavigationRoutes.Login.route, modifier = modifier) {

        // Login Screen
        composable(NavigationRoutes.Login.route) {
            LoginScreen(
                onForgotPassword = { navController.navigate(NavigationRoutes.ForgotPassword.route) },
                onCreateAccount = { navController.navigate(NavigationRoutes.CreateAccount.route) },
                onLoginSuccess = { userId ->
                    navController.navigate(NavigationRoutes.LandingPage.createRoute(userId)) {
                        popUpTo(NavigationRoutes.Login.route) { inclusive = true }
                    }
                },
                onLoginFailed = { navController.navigate(NavigationRoutes.LoginFailed.route) }
            )
        }

        // Landing Page Screen with BottomNavigationBar
        composable(NavigationRoutes.LandingPage.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            userId?.let {
                LandingPageScreen(
                    userId = it,
                    navController = navController,
                    onLogout = {
                        navController.navigate(NavigationRoutes.Login.route) {
                            popUpTo(NavigationRoutes.LandingPage.route) { inclusive = true }
                        }
                    }
                )
            } ?: navController.navigate(NavigationRoutes.Login.route) {
                popUpTo(0) { inclusive = true }  // Clear the entire backstack
            }
        }

        // Forgot Password Screen
        composable(NavigationRoutes.ForgotPassword.route) {
            ForgotPasswordScreen(
                onPasswordReset = {
                    navController.popBackStack() // Go back to the login screen
                },
                onResetFailed = {
                    // Handle reset failure, e.g., show a message
                },
                onBackToLogin = {
                    navController.popBackStack() // Go back to the login screen
                }
            )
        }

        // Create Account Screen
        composable(NavigationRoutes.CreateAccount.route) {
            CreateAccountScreen(
                onLoginInstead = {
                    navController.popBackStack() // Go back to the login screen
                }
            )
        }

        // Login Failed Screen
        composable(NavigationRoutes.LoginFailed.route) {
            LoginFailedScreen(
                onRetryLogin = {
                    navController.popBackStack() // Go back to the login screen
                },
                onForgotPassword = {
                    navController.navigate(NavigationRoutes.ForgotPassword.route)
                }
            )
        }

        // Matches Screen
        composable(NavigationRoutes.Matches.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            userId?.let {
                MatchesScreen(
                    userId = it,
                    navController = navController,
                    matches = listOf("Match 1", "Match 2", "Match 3"),  // Replace with actual data
                    onMatchSelected = {
                        // Handle match selection, e.g., navigate to a match detail screen
                    }
                )
            } ?: navController.navigate(NavigationRoutes.Login.route) {
                popUpTo(0) { inclusive = true }  // Clear the entire backstack
            }
        }

        // Profile Screen
        composable(
            route = NavigationRoutes.Profile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            if (userId != null) {
                ProfileScreen(
                    userId = userId,
                    navController = navController,
                )
            } else {
                navController.navigate(NavigationRoutes.Login.route) {
                    popUpTo(0) { inclusive = true }  // Clear the entire backstack
                }
            }
        }

        // Profile Update Screen
        composable(
            route = NavigationRoutes.ProfileUpdate.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: throw IllegalStateException("User ID is required for ProfileUpdateScreen")
            ProfileUpdateScreen(userId = userId, navController = navController)
        }

        // Settings Screen
        composable(
            route = NavigationRoutes.Settings.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            if (userId != null) {
                SettingsScreen(
                    userId = userId,
                    navController = navController
                )
            } else {
                // Optionally navigate to an error screen or the login screen
                navController.navigate(NavigationRoutes.Login.route)
            }
        }
    }
}