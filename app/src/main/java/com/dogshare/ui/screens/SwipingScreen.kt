package com.dogshare.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.dogshare.ui.components.SwipeableCard
import com.dogshare.ui.components.ToastUtils
import com.dogshare.ui.components.rememberSwipeableCardState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SwipingScreen(
    userId: String,
    viewModel: SwipingScreenViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val firestore = FirebaseFirestore.getInstance()

    var firstSwipe by remember { mutableStateOf(true) }
    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var currentPhotoIndex by remember { mutableStateOf(0) }  // Tracks the current photo

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

    // Fetch dog photos
    LaunchedEffect(Unit) {
        viewModel.fetchDogPhotos("nNC9XgA77_sgn-co85RYbEaawFa-zopkHRokr7HkSN0")
    }

    // UI display
    if (viewModel.dogPhotos.isEmpty() || currentPhotoIndex >= viewModel.dogPhotos.size) {
        // No more photos to show or still loading
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            val state = rememberSwipeableCardState()
            SwipeableCard(
                userId = userId,
                state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                onSwipe = { direction ->
                    when (direction) {
                        com.dogshare.ui.components.Direction.LEFT -> println("Swiped left: Dislike")
                        com.dogshare.ui.components.Direction.RIGHT -> println("Swiped right: Like")
                        com.dogshare.ui.components.Direction.UP -> println("Swiped up: Skip")
                        com.dogshare.ui.components.Direction.DOWN -> println("Swiped down: Super Like")
                    }
                    // Move to the next photo after each swipe
                    currentPhotoIndex = (currentPhotoIndex + 1) % viewModel.dogPhotos.size
                }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = viewModel.dogPhotos[currentPhotoIndex].urls.regular
                    ),
                    contentDescription = "Dog Image",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

// Function to capture user location
fun captureLocation(
    context: android.content.Context,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationCaptured: (Double, Double) -> Unit
) {
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                onLocationCaptured(it.latitude, it.longitude)
            } ?: run {
                ToastUtils.showToast(context, "Failed to get location.")
            }
        }.addOnFailureListener {
            ToastUtils.showToast(context, "Location retrieval failed.")
        }
    }
}

// Function to store location in Firebase Firestore
fun storeLocationInFirestore(
    userId: String,
    lat: Double,
    lon: Double,
    firestore: FirebaseFirestore
) {
    val userLocation = hashMapOf(
        "latitude" to lat,
        "longitude" to lon,
        "timestamp" to System.currentTimeMillis()
    )

    firestore.collection("user_locations")
        .document(userId)
        .set(userLocation)
        .addOnSuccessListener {
            println("Location stored successfully")
        }
        .addOnFailureListener { e ->
            println("Failed to store location: ${e.message}")
        }
}
