package com.dogshare.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import com.dogshare.ui.composables.QuestionnaireSection
import com.dogshare.ui.screens.*

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String,
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
                onGoogleSignIn = {
                    navController.navigate(NavigationRoutes.GoogleSignIn.route)
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

        composable(
            route = NavigationRoutes.LandingPage.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            if (userId == null || userId == "{userId}") {
                Log.e("NavigationError", "UserId is null or placeholder on navigation. Navigating to Login.")
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

        composable(
            route = NavigationRoutes.Matches.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            MatchesScreen(userId = userId, navController = navController, matches = listOf("Match1", "Match2")) { match ->
                // Handle match selected logic
            }
        }

        composable(
            route = NavigationRoutes.Profile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
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

        composable(
            route = NavigationRoutes.WelcomeScreen.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            OnboardingScreen(
                username = "User", // Fetch the actual username if available
                navController = navController,
                onStartQuestionnaire = {
                    navController.navigate(NavigationRoutes.Questionnaire.createRoute(userId)) {
                        popUpTo(NavigationRoutes.WelcomeScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = NavigationRoutes.Questionnaire.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            QuestionnaireSection(
                userId = userId,
                navController = navController,
                onPreferencesSaved = {
                    navController.navigate(NavigationRoutes.LandingPage.createRoute(userId)) {
                        popUpTo(NavigationRoutes.Questionnaire.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
