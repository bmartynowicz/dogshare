package com.dogshare.ui.composables

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
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
import com.dogshare.navigation.NavigationRoutes
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.dogshare.ui.components.ToastUtils
import com.dogshare.repository.PreferencesRepository
import com.google.firebase.firestore.SetOptions
import org.koin.androidx.compose.getKoin
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionnaireSection(
    userId: String,
    navController: NavHostController,
    onPreferencesSaved: () -> Unit
) {
    val context = LocalContext.current
    val preferencesRepository: PreferencesRepository = get()
    val db = FirebaseFirestore.getInstance()

    var experience by remember { mutableStateOf("") }
    var careFrequency by remember { mutableStateOf("") }
    var breedPreference by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }

    // Function to save preferences along with geolocation
    fun savePreferencesWithLocation() {
        if (latitude != null && longitude != null) {
            val userProfile = mapOf(
                "experience" to experience.trim(),
                "frequency" to careFrequency.trim(),
                "breed" to breedPreference.trim(),
                "latitude" to latitude,
                "longitude" to longitude,
                "isQuestionnaireCompleted" to true  // Add this line
            )

            db.collection("profiles").document(userId).set(userProfile, SetOptions.merge())
                .addOnSuccessListener {
                    preferencesRepository.setUserId(userId)
                    preferencesRepository.setFirstLoginCompleted()
                    Log.d("QuestionnaireSection", "First login completed set to true for userId: $userId")
                    onPreferencesSaved()
                    ToastUtils.showToast(context, "Profile updated successfully!")
                    navController.navigate(NavigationRoutes.LandingPage.createRoute(userId)) {
                        popUpTo(NavigationRoutes.Questionnaire.route) { inclusive = true }
                    }
                }
                .addOnFailureListener {
                    ToastUtils.showToast(context, "Failed to update profile, please try again.")
                }
        } else {
            ToastUtils.showToast(context, "Location not available, please retry.")
        }
    }

    fun validateAndSavePreferences() {
        if (experience.isBlank() || careFrequency.isBlank() || breedPreference.isBlank()) {
            ToastUtils.showToast(context, "Please fill all fields before saving.")
            return
        }
        savePreferencesWithLocation()
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
                ToastUtils.showToast(context, "Error retrieving location. Please try again later.")
            }
    }

    // Launcher to request location permission
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        if (granted) {
            captureLocation()
        } else {
            ToastUtils.showToast(context, "Location permission denied.")
        }
    }

    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            captureLocation()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("What's your experience with dogs?")
        TextField(
            value = experience,
            onValueChange = { experience = it },
            label = { Text("Experience") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Text("How often can you care for a dog?")
        TextField(
            value = careFrequency,
            onValueChange = { careFrequency = it },
            label = { Text("Care Frequency") },
            placeholder = { Text("e.g., Daily, Weekly") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Text("Do you have any breed preferences?")
        TextField(
            value = breedPreference,
            onValueChange = { breedPreference = it },
            label = { Text("Breed Preference") },
            placeholder = { Text("e.g., Labrador, Beagle") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = { validateAndSavePreferences() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            Text("Save Preferences")
        }
    }
}
