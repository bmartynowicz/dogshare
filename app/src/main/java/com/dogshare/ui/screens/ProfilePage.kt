package com.dogshare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfilePage(username: String, onPreferencesUpdated: () -> Unit, onLogout: () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    var experience by remember { mutableStateOf("") }
    var careFrequency by remember { mutableStateOf("") }
    var breedPreference by remember { mutableStateOf("") }
    var preferencesLoaded by remember { mutableStateOf(false) }
    var loadError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        db.collection("user_preferences").document(username).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    experience = document.getString("Experience") ?: ""
                    careFrequency = document.getString("Frequency") ?: ""
                    breedPreference = document.getString("Breed") ?: ""
                    preferencesLoaded = true
                } else {
                    loadError = true
                }
            }
            .addOnFailureListener {
                loadError = true
            }
    }

    fun savePreferences() {
        db.collection("user_preferences").document(username).set(
            mapOf(
                "Experience" to experience,
                "Frequency" to careFrequency,
                "Breed" to breedPreference
            )
        ).addOnSuccessListener {
            onPreferencesUpdated()
        }.addOnFailureListener {
            // Handle failure
        }
    }

    if (loadError) {
        Text("Error loading profile data.")
    } else if (!preferencesLoaded) {
        Text("Loading profile data...")
    } else {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("What’s your experience with dogs?")
            TextField(
                value = experience,
                onValueChange = { experience = it },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            Text("How often can you care for a dog?")
            TextField(
                value = careFrequency,
                onValueChange = { careFrequency = it },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            Text("Do you have any breed preferences?")
            TextField(
                value = breedPreference,
                onValueChange = { breedPreference = it },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            Button(
                onClick = { savePreferences() },
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
            ) {
                Text("Save Preferences")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    auth.signOut()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            ) {
                Text("Log Out")
            }
        }
    }
}
