package com.dogshare.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun RequestLocationPermission(onPermissionGranted: () -> Unit) {
    // This launcher will handle the permission request
    val locationPermissionRequest = rememberLauncherForActivityResult(
        contract = RequestPermission(),
        onResult = { granted ->
            if (granted) {
                onPermissionGranted()  // Execute this block if permission is granted
            } else {
                // Handle the case where the user denies the permission
            }
        }
    )

    // UI element to trigger the permission request
    Button(onClick = { locationPermissionRequest.launch(android.Manifest.permission.ACCESS_FINE_LOCATION) }) {
        Text("Allow Location Access")
    }
}
