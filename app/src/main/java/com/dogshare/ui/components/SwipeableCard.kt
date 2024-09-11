package com.dogshare.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.roundToInt

// Direction enum to define swipe directions
enum class Direction { LEFT, RIGHT, UP, DOWN }

// Main Composable function to create a swipeable card
@Composable
fun SwipeableCard(
    userId: String,
    state: SwipeableCardState = rememberSwipeableCardState(),
    modifier: Modifier = Modifier,
    onSwipe: (Direction) -> Unit,
    content: @Composable () -> Unit
) {
    // Context and location services
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val firestore = FirebaseFirestore.getInstance() // Initialize Firestore

    var firstSwipe by remember { mutableStateOf(true) }
    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    // Location permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                // Permission granted, capture location
                captureLocation(context, fusedLocationClient) { lat, lon ->
                    location = Pair(lat, lon)
                    storeLocationInFirestore(userId, lat, lon, firestore) // Store location in Firestore
                }
            } else {
                // Permission denied, handle accordingly (Replace with your Toast utility or similar)
                println("Location permission denied.")
            }
        }
    )

    // Create a swipeable box
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        state.onDragEnd()
                        state.swipeDirection?.let { direction ->
                            if (firstSwipe) {
                                // Capture location on the first swipe
                                firstSwipe = false
                                if (ActivityCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    // Request location permission if not granted
                                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                } else {
                                    // Permission already granted, capture location
                                    captureLocation(context, fusedLocationClient) { lat, lon ->
                                        location = Pair(lat, lon)
                                        storeLocationInFirestore(userId, lat, lon, firestore) // Store location in Firestore
                                    }
                                }
                            }
                            onSwipe(direction) // Perform action based on swipe direction
                        }
                    },
                    onDrag = { _, dragAmount ->
                        state.onDrag(dragAmount)
                    }
                )
            }
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.height) {
                    val offset = state.getOffset() // Use the getOffset() function
                    placeable.placeRelative(offset.x.roundToInt(), offset.y.roundToInt())
                }
            }
    ) {
        content()
    }
}

// SwipeableCardState class to handle drag gestures and swipe states
class SwipeableCardState {
    private var offsetX by mutableStateOf(0f)
    private var offsetY by mutableStateOf(0f)
    var swipeDirection: Direction? by mutableStateOf(null)

    fun onDrag(dragAmount: Offset) {
        offsetX += dragAmount.x
        offsetY += dragAmount.y
    }

    fun onDragEnd() {
        swipeDirection = when {
            offsetX > 100 -> Direction.RIGHT
            offsetX < -100 -> Direction.LEFT
            offsetY > 100 -> Direction.DOWN
            offsetY < -100 -> Direction.UP
            else -> null
        }
        resetOffsets()
    }

    private fun resetOffsets() {
        offsetX = 0f
        offsetY = 0f
    }

    fun getOffset(): Offset {
        return Offset(offsetX, offsetY)
    }
}

// Function to capture user location
fun captureLocation(context: Context, fusedLocationClient: FusedLocationProviderClient, onLocationCaptured: (Double, Double) -> Unit) {
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                onLocationCaptured(it.latitude, it.longitude)
            } ?: run {
                println("Failed to get location.")
            }
        }.addOnFailureListener {
            println("Location retrieval failed.")
        }
    } else {
        println("Location permission not granted.")
    }
}

// Function to store location in Firebase Firestore
fun storeLocationInFirestore(userId: String, lat: Double, lon: Double, firestore: FirebaseFirestore) {
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

@Composable
fun rememberSwipeableCardState(): SwipeableCardState {
    return remember { SwipeableCardState() }
}
