package com.dogshare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dogshare.R
import com.dogshare.ui.components.SwipeableCard
import com.dogshare.ui.components.rememberSwipeableCardState

@Composable
fun SwipingScreen(
    userId: String?,
    navController: NavController
) {
    Scaffold(
        topBar = {
        },
        // Removed the BottomNavigationBar from here
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            SwipeableCardContent()
        }
    }
}

@Composable
fun SwipeableCardContent() {
    val swipeableCardState = rememberSwipeableCardState()

    SwipeableCard(
        state = swipeableCardState,
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        onSwipe = { direction ->
            // Handle swipe direction here
            println("Swiped $direction")
        }
    ) {
        // The content inside the swipeable card
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Swipe Me")
        }
    }
}