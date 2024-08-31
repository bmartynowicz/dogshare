package com.dogshare.ui.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dogshare.navigation.NavigationRoutes

@Composable
fun MatchesScreen(navController: NavController, userId: String?) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Matches Screen for User ID: $userId")

        Button(onClick = {
            navController.navigate(NavigationRoutes.Swiping.createRoute(userId ?: ""))
        }) {
            Text(text = "Start Swiping")
        }
    }
}
