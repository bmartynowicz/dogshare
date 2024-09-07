package com.dogshare.navigation

import androidx.navigation.NavController

fun NavController.navigateToLandingPage(userId: String) {
    this.navigate(NavigationRoutes.LandingPage.createRoute(userId))
}

fun NavController.navigateToProfile(userId: String) {
    this.navigate(NavigationRoutes.Profile.createRoute(userId))
}

fun NavController.navigateToMatches(userId: String) {
    this.navigate(NavigationRoutes.Matches.createRoute(userId))
}