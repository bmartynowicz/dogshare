package com.dogshare.navigation

sealed class NavigationRoutes(val route: String) {
    object Login : NavigationRoutes("login")
    object LandingPage : NavigationRoutes("landingPage/{userId}") {
        fun createRoute(userId: String) = "landingPage/$userId"
    }
    object Settings : NavigationRoutes("settings/{userId}") {
        fun createRoute(userId: String) = "settings/$userId"
    }
    object Matches : NavigationRoutes("matches/{userId}") {
        fun createRoute(userId: String) = "matches/$userId"
    }
    object Profile : NavigationRoutes("profile/{userId}") {
        fun createRoute(userId: String) = "profile/$userId"
    }
    object ForgotPassword : NavigationRoutes("forgotPassword")
    object CreateAccount : NavigationRoutes("createAccount")
    object LoginFailed : NavigationRoutes("loginFailed")
    object GoogleSignIn : NavigationRoutes("googleSignIn")
}
