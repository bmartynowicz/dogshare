package com.dogshare.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dogshare.ui.screens.*

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String,
    onGoogleSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Log.d("Navigation", "Start destination set to: $startDestination")

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(NavigationRoutes.Login.route) {
            LoginScreen(
                navController = navController,
                onGoogleSignIn = onGoogleSignIn,
                onCreateAccount = {
                    navController.navigate(NavigationRoutes.CreateAccount.route)
                },
                onLoginSuccess = { userId ->
                    navController.navigate(NavigationRoutes.LandingPage.createRoute(userId)) {
                        popUpTo(NavigationRoutes.Login.route) { inclusive = true }
                    }
                },
                onLoginFailed = {
                    navController.navigate(NavigationRoutes.LoginFailed.route)
                },
                onForgotPassword = {
                    navController.navigate(NavigationRoutes.ForgotPassword.route)
                }
            )
        }

        composable(NavigationRoutes.ForgotPassword.route) {
            ForgotPasswordScreen(
                onPasswordReset = {
                    navController.navigate(NavigationRoutes.Login.route)
                },
                onResetFailed = { /* Handling of password reset failure */ },
                onBackToLogin = {
                    navController.navigateUp()
                }
            )
        }

        composable(NavigationRoutes.LandingPage.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            if (userId == null) {
                Log.e("NavigationError", "UserId is null on app restart. Navigating to Login.")
                navController.navigate(NavigationRoutes.Login.route) {
                    popUpTo(NavigationRoutes.LandingPage.route) { inclusive = true }
                }
            } else {
                LandingPageScreen(
                    userId = userId,
                    navController = navController,
                    onLogout = {
                        navController.navigate(NavigationRoutes.Login.route) {
                            popUpTo(NavigationRoutes.LandingPage.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(NavigationRoutes.Settings.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            SettingsScreen(userId = userId, navController = navController)
        }

        composable(NavigationRoutes.Matches.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            MatchesScreen(userId = userId, navController = navController, matches = listOf("Match1", "Match2")) { match ->
                // Handle match selected logic
            }
        }

        composable(NavigationRoutes.Profile.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ProfileScreen(userId = userId, navController = navController)
        }

        composable(NavigationRoutes.CreateAccount.route) {
            CreateAccountScreen(
                onLoginInstead = {
                    navController.navigate(NavigationRoutes.Login.route) {
                        popUpTo(NavigationRoutes.CreateAccount.route) { inclusive = true }
                    }
                },
                navController = navController
            )
        }

        composable(NavigationRoutes.LoginFailed.route) {
            LoginFailedScreen(
                onRetryLogin = { navController.navigate(NavigationRoutes.Login.route) },
                onForgotPassword = { navController.navigate(NavigationRoutes.ForgotPassword.route) }
            )
        }
    }
}
