package com.dogshare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dogshare.R
import com.dogshare.ui.components.BottomNavigationBar
import com.dogshare.ui.utils.checkUserPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingPage(username: String, navController: NavController) {
    val typography = MaterialTheme.typography
    var showQuestionnaire by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(username) {
        checkUserPreferences(username) { hasPreferences ->
            showQuestionnaire = !hasPreferences
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("DogShare", style = typography.headlineSmall) },
                navigationIcon = {
                    AsyncImage(
                        model = R.drawable.logo_round,
                        contentDescription = "App Icon",
                        modifier = Modifier.size(40.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isLoading) {
                    Text("Loading...")
                } else if (showQuestionnaire) {
                    QuestionnaireSection(username = username, onPreferencesSaved = {
                        navController.navigate("Landing") {
                            popUpTo("Landing") { inclusive = true }
                        }
                    })
                } else {
                    SwipingSection()
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    )
}
