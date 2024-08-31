package com.dogshare.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dogshare.ui.screens.LoginScreen  // Import LoginScreen
import com.dogshare.ui.screens.LandingPageScreen  // Import other screens as needed
import com.dogshare.ui.screens.ProfileScreen
import com.dogshare.ui.screens.SettingsScreen
import com.dogshare.ui.screens.MatchesScreen
import com.dogshare.navigation.NavigationRoutes
import com.dogshare.ui.screens.CreateAccountScreen
import com.dogshare.ui.screens.ForgotPasswordScreen
import com.dogshare.ui.screens.LoginFailedScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavigationRoutes.Login.route) {
        composable(NavigationRoutes.Login.route) {
            LoginScreen(
                onForgotPassword = { navController.navigate(NavigationRoutes.ForgotPassword.route) },
                onCreateAccount = { navController.navigate(NavigationRoutes.CreateAccount.route) },
                onLoginSuccess = { userId ->
                    navController.navigate(NavigationRoutes.LandingPage.createRoute(userId))
                },
                onLoginFailed = { navController.navigate(NavigationRoutes.LoginFailed.route) }
            )
        }
        composable(
            route = NavigationRoutes.LandingPage.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            LandingPageScreen(navController = navController, userId = userId)
        }
        composable(
            route = NavigationRoutes.Matches.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            MatchesScreen(navController = navController, userId = userId)
        }
        composable(
            route = NavigationRoutes.Profile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            ProfileScreen(navController = navController, userId = userId)
        }
        composable(NavigationRoutes.Settings.route) {
            SettingsScreen(navController = navController)
        }
        composable(NavigationRoutes.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController)
        }
        composable(NavigationRoutes.CreateAccount.route) {
            CreateAccountScreen(navController = navController)
        }
        composable(NavigationRoutes.LoginFailed.route) {
            LoginFailedScreen(navController = navController)
        }
    }
}
