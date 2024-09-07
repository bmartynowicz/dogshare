package com.dogshare.ui.components

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dogshare.navigation.NavigationRoutes

@Composable
fun BottomNavigationBar(navController: NavController, userId: String) {
    val items = listOf(
        BottomNavItem(NavigationRoutes.LandingPage.createRoute(userId), Icons.Default.Home, "Home"),
        BottomNavItem(NavigationRoutes.Matches.createRoute(userId), Icons.Default.Favorite, "Matches"),
        BottomNavItem(NavigationRoutes.Settings.createRoute(userId), Icons.Default.Settings, "Settings"),
        BottomNavItem(NavigationRoutes.Profile.createRoute(userId), Icons.Default.AccountCircle, "Profile")
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (currentRoute == item.route) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (currentRoute == item.route) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    Log.d("BottomNavigationBar", "Button clicked: ${item.label}")
                    Log.d("BottomNavigationBar", "Current route: $currentRoute")
                    Log.d("BottomNavigationBar", "Navigating to: ${item.route}")
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                alwaysShowLabel = true
            )
        }
    }
}

data class BottomNavItem(val route: String, val icon: ImageVector, val label: String)
