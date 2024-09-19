package com.dogshare.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.dogshare.R
import com.dogshare.models.DogProfile
import com.dogshare.ui.components.Direction
import com.dogshare.ui.components.SwipeableCard
import com.dogshare.ui.components.ToastUtils
import com.dogshare.ui.components.captureLocation
import com.dogshare.ui.components.rememberSwipeableCardState
import com.dogshare.ui.components.storeLocationInFirestore
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun SwipingScreen(
    userId: String,
    viewModel: SwipingScreenViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val firestore = FirebaseFirestore.getInstance()
    val coroutineScope = rememberCoroutineScope()

    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    // Location permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                captureLocation(context, fusedLocationClient) { lat, lon ->
                    location = Pair(lat, lon)
                    storeLocationInFirestore(userId, lat, lon, firestore)
                }
            } else {
                ToastUtils.showToast(context, "Location permission denied.")
            }
        }
    )

    // Request location permission if not already granted
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            captureLocation(context, fusedLocationClient) { lat, lon ->
                location = Pair(lat, lon)
                storeLocationInFirestore(userId, lat, lon, firestore)
            }
        }
    }

    LaunchedEffect(viewModel.currentPhotoIndex) {
        Log.i("SwipingScreen", "Current photo index: ${viewModel.currentPhotoIndex}")
    }

    // Fetch dog photos
    LaunchedEffect(Unit) {
        viewModel.fetchDogProfiles("nNC9XgA77_sgn-co85RYbEaawFa-zopkHRokr7HkSN0")
    }


    if (viewModel.dogProfiles.isEmpty() || viewModel.currentPhotoIndex >= viewModel.dogProfiles.size) {
        // Show loading indicator if there are no photos or still loading
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Full-screen swipeable card image
            val state = rememberSwipeableCardState()

            SwipeableCard(
                userId = userId,
                DogProfile = viewModel.dogProfiles.map { it.imageUrl },  // List of dog photo URLs
                currentPhotoIndex = viewModel.currentPhotoIndex,
                state = state,
                modifier = Modifier.fillMaxSize(),
                onSwipeComplete = { direction ->
                    coroutineScope.launch {
                        when (direction) {
                            Direction.LEFT -> println("Swiped left: Dislike")
                            Direction.RIGHT -> println("Swiped right: Like")
                            Direction.UP -> println("Swiped up: Skip")
                            Direction.DOWN -> println("Swiped down: Super Like")
                        }
                        viewModel.updatePhotoIndex()
                        Log.i("SwipingScreen", "Current photo index: ${viewModel.currentPhotoIndex}")
                    }
                }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = viewModel.dogProfiles[viewModel.currentPhotoIndex].imageUrl
                    ),
                    contentDescription = "Dog Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Overlay Logo and Title on top of the image
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)  // Align the logo and title at the top of the image
                    .padding(top = 16.dp),  // Padding for the logo and title
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dogshare logo
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_round),
                    contentDescription = "Dogshare Logo",
                    modifier = Modifier.size(64.dp)  // Adjust logo size as needed
                )
                // Dogshare title
                Text(
                    text = "Dogshare",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,  // White color to make the title visible over the image
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

