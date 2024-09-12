package com.dogshare.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.dogshare.models.DogProfile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// Enum for swipe directions
enum class Direction { LEFT, RIGHT, UP, DOWN }

// SwipeableCard Composable function
@Composable
fun SwipeableCard(
    userId: String,
    DogProfile: List<String>, // List of dog photo URLs
    currentPhotoIndex: Int,  // Index of the current photo
    state: SwipeableCardState = rememberSwipeableCardState(),
    onSwipeComplete: (Direction) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val firestore = FirebaseFirestore.getInstance()
    val coroutineScope = rememberCoroutineScope()

    var firstSwipe by remember { mutableStateOf(true) }
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
                // Handle permission denied
                println("Location permission denied. Please grant the permission for enhanced experience.")
            }
        }
    )

    // Swipeable Card Logic with animation
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        state.onDragEnd()
                        state.swipeDirection?.let { direction ->
                            // Ensure the first swipe captures location
                            if (firstSwipe) {
                                firstSwipe = false
                            }
                            // Store the swipe action in Firestore
                            coroutineScope.launch {
                                storeSwipeActionInFirestore(userId, DogProfile[currentPhotoIndex], direction, firestore)
                                // Animate swipe out and move to the next card
                                state.animateSwipeOut(direction)
                                onSwipeComplete(direction) // Notify that a swipe has occurred
                            }
                        }
                    },
                    onDrag = { _, dragAmount ->
                        coroutineScope.launch {
                            state.onDrag(dragAmount)  // Handle drag
                        }
                    }
                )
            }
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.height) {
                    val offset = state.getOffset()
                    placeable.placeRelative(offset.x.roundToInt(), offset.y.roundToInt())
                }
            }
    ) {
        content()
    }
}

// SwipeableCardState to handle swipe behavior and animation
class SwipeableCardState {
    private var offsetX = Animatable(0f)
    private var offsetY = Animatable(0f)
    var swipeDirection: Direction? by mutableStateOf(null)

    // Handle dragging
    suspend fun onDrag(dragAmount: Offset) {
        offsetX.snapTo(offsetX.value + dragAmount.x)
        offsetY.snapTo(offsetY.value + dragAmount.y)
    }

    // Handle drag end and detect swipe direction
    fun onDragEnd() {
        swipeDirection = when {
            offsetX.value > 100 -> Direction.RIGHT
            offsetX.value < -100 -> Direction.LEFT
            offsetY.value > 100 -> Direction.DOWN
            offsetY.value < -100 -> Direction.UP
            else -> null
        }
    }

    // Animate swipe out of the screen
    suspend fun animateSwipeOut(direction: Direction) {
        val animationDuration = 300 // Duration of the animation in milliseconds
        when (direction) {
            Direction.LEFT -> offsetX.animateTo(-1000f, animationSpec = tween(durationMillis = animationDuration))
            Direction.RIGHT -> offsetX.animateTo(1000f, animationSpec = tween(durationMillis = animationDuration))
            Direction.UP -> offsetY.animateTo(-1000f, animationSpec = tween(durationMillis = animationDuration))
            Direction.DOWN -> offsetY.animateTo(1000f, animationSpec = tween(durationMillis = animationDuration))
        }
        // Reset offsets after animation completes
        resetOffsets()
    }

    // Reset offsets after a swipe or drag
    private suspend fun resetOffsets() {
        offsetX.animateTo(0f)
        offsetY.animateTo(0f)
    }

    fun getOffset(): Offset {
        return Offset(offsetX.value, offsetY.value)
    }
}

// Function to capture user location if permission is granted
fun captureLocation(context: Context, fusedLocationClient: FusedLocationProviderClient, onLocationCaptured: (Double, Double) -> Unit) {
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                onLocationCaptured(it.latitude, it.longitude)
            } ?: run {
                println("Failed to get location.")
            }
        }.addOnFailureListener {
            println("Location retrieval failed: ${it.message}")
        }
    } else {
        println("Location permission not granted.")
    }
}

// Function to store swipe action in Firestore
fun storeSwipeActionInFirestore(
    userId: String,
    dogPhotoUrl: String,
    direction: Direction,
    firestore: FirebaseFirestore
) {
    val swipeAction = hashMapOf(
        "photoUrl" to dogPhotoUrl,
        "direction" to direction.name,
        "timestamp" to System.currentTimeMillis()
    )

    // Use set() with a generated ID instead of add()
    firestore.collection("swipe_actions")
        .document(userId)
        .collection("actions")
        .document()  // Create a new document with a random ID
        .set(swipeAction)  // Using set() instead of add()
        .addOnSuccessListener {
            println("Swipe action stored successfully")
        }
        .addOnFailureListener { e ->
            println("Failed to store swipe action: ${e.message}")
        }
}


// Function to store location in Firestore
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

// Remember SwipeableCardState
@Composable
fun rememberSwipeableCardState(): SwipeableCardState {
    return remember { SwipeableCardState() }
}
