package com.dogshare.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.dogshare.R
import com.dogshare.ui.RequestLocationPermission
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipingSection(
    userId: String,
    navController: NavHostController
) {
    val context = LocalContext.current

    // State to store location data
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    var locationUpdated by remember { mutableStateOf(false) }

    // Request location permission when the screen is loaded
    RequestLocationPermission(onPermissionGranted = {
        // No need to capture location immediately; capture it when user swipes
    })

    // Capture location and update it the first time the user swipes
    Button(onClick = {
        if (!locationUpdated) {
            captureLocation(context) { lat, lon ->
                latitude = lat
                longitude = lon
                updateUserLocationInFirebase(userId, lat, lon)
                locationUpdated = true // Mark location as updated after swiping
            }
        }
        // Proceed with swiping logic
        if (latitude != null && longitude != null) {
            initiateDogSwiping(latitude, longitude)
            // Add any navigation logic after swiping if needed
            // navController.navigate(/* route to next screen or perform additional navigation*/)
        } else {
            // Handle case where location is not available
            println("Location is not available yet")
        }
    }) {
        Text("Start Swiping")
    }

    // Load the image using Coil's rememberAsyncImagePainter
    val imagePainter = rememberAsyncImagePainter(R.drawable.dog_image)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray), // Optional background color
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = imagePainter,
            contentDescription = "Dog Image",
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = "Swipe right if you like the dog.",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@SuppressLint("MissingPermission")
fun captureLocation(context: Context, onLocationCaptured: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            location?.let {
                val latitude = it.latitude
                val longitude = it.longitude
                onLocationCaptured(latitude, longitude)
            }
        }
        .addOnFailureListener {
            // Handle the failure case, e.g., show an error message or retry
        }
}

fun updateUserLocationInFirebase(userId: String, latitude: Double, longitude: Double) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users").document(userId)
        .update(mapOf(
            "latitude" to latitude,
            "longitude" to longitude
        ))
        .addOnSuccessListener {
            println("Location updated successfully.")
        }
        .addOnFailureListener { e ->
            println("Error updating location: $e")
        }
}

fun initiateDogSwiping(latitude: Double?, longitude: Double?) {
    // Logic to start the dog swiping/matching process
    // This could include fetching location-based matches from Firebase
    if (latitude != null && longitude != null) {
        // Use latitude and longitude to filter dogs within the user's area
        println("Starting swiping with location: $latitude, $longitude")
    }
}
