package com.dogshare.ui.composables

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionnaireSection(
    userId: String,
    navController: NavHostController,
    onPreferencesSaved: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var experience by remember { mutableStateOf("") }
    var careFrequency by remember { mutableStateOf("") }
    var breedPreference by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }

    // Function to save preferences along with geolocation
    fun savePreferencesWithLocation() {
        if (latitude != null && longitude != null) {
            db.collection("user_preferences").document(userId).set(
                mapOf(
                    "Experience" to experience,
                    "Frequency" to careFrequency,
                    "Breed" to breedPreference,
                    "Latitude" to latitude,
                    "Longitude" to longitude
                )
            ).addOnSuccessListener {
                onPreferencesSaved()
                // Navigate to the SwipingSection after saving preferences
                navController.navigate("swiping/${userId}")
            }.addOnFailureListener {
                // Handle failure (e.g., show a message)
            }
        } else {
            // Handle case where location is not available
            // Show a message to the user or retry
        }
    }

    // Capture the user's location when the questionnaire is filled out for the first time
    @SuppressLint("MissingPermission")
    fun captureLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    latitude = it.latitude
                    longitude = it.longitude
                }
            }
            .addOnFailureListener {
                // Handle the failure case, e.g., show an error message or retry
            }
    }

    // Launcher to request location permission
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        if (granted) {
            // Permission is granted, capture the location
            captureLocation()
        } else {
            // Handle the case where the permission is denied
            // Show a message or navigate away
        }
    }

    // Call captureLocation when the composable is first loaded
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the missing permissions
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            // Permissions are already granted, capture the location
            captureLocation()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("What’s your experience with dogs?")
        TextField(
            value = experience,
            onValueChange = { experience = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Text("How often can you care for a dog?")
        TextField(
            value = careFrequency,
            onValueChange = { careFrequency = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Text("Do you have any breed preferences?")
        TextField(
            value = breedPreference,
            onValueChange = { breedPreference = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = { savePreferencesWithLocation() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Text("Save Preferences")
        }
    }
}
