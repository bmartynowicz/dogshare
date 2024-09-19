package com.dogshare.ui.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dogshare.navigation.NavigationRoutes

@Composable
fun BottomNavigationBar(navController: NavController, userId: String) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Matches,
        BottomNavItem.NewFeature,
        BottomNavItem.Profile
    )

    BottomNavigation {
        val currentRoute = currentRoute(navController)
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.baseRoute,
                onClick = {
                    val route = when (item) {
                        is BottomNavItem.Home -> item.createRoute(userId)
                        is BottomNavItem.Matches -> item.createRoute(userId)
                        is BottomNavItem.Profile -> item.createRoute(userId)
                        else -> item.route
                    }

                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


// Define your BottomNavItem with routes and icons
sealed class BottomNavItem(
    var title: String,
    var icon: ImageVector,
    var route: String,
    var baseRoute: String
) {
    object Home : BottomNavItem("Home", Icons.Default.Home, "landingPage/{$USER_ID}", "landingPage") {
        fun createRoute(userId: String) = "landingPage/$userId"
    }
    object Matches : BottomNavItem("Matches", Icons.Default.Search, "matches/{$USER_ID}", "matches") {
        fun createRoute(userId: String) = "matches/$userId"
    }
    object NewFeature : BottomNavItem("New", Icons.Default.AddCircle, NavigationRoutes.NewFeature.route, "new_feature")
    object Profile : BottomNavItem("Profile", Icons.Default.Person, NavigationRoutes.Profile.route, "profile") {
        fun createRoute(userId: String) = NavigationRoutes.Profile.createRoute(userId)
    }

    companion object {
        const val USER_ID = "userId"
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val route = navBackStackEntry?.destination?.route
    return route?.substringBefore("/")
}


