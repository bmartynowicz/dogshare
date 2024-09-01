package com.dogshare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dogshare.ui.components.BottomNavigationBar

@Composable
fun MatchesScreen(
    userId: String,
    navController: NavController,
    matches: List<String>,  // Replace String with your Match data model
    onMatchSelected: (String) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, userId = userId)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Your Matches", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(matches) { match ->
                    MatchItem(match = match, onMatchSelected = onMatchSelected)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }
}

@Composable
fun MatchItem(match: String, onMatchSelected: (String) -> Unit) {
    TextButton(
        onClick = { onMatchSelected(match) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = match)  // Customize to display match details
    }
}
