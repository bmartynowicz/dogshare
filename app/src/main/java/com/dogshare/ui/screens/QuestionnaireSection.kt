package com.dogshare.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionnaireSection(username: String, onPreferencesSaved: () -> Unit) {
    val db = FirebaseFirestore.getInstance()

    var experience by remember { mutableStateOf("") }
    var careFrequency by remember { mutableStateOf("") }
    var breedPreference by remember { mutableStateOf("") }

    fun savePreferences() {
        db.collection("user_preferences").document(username).set(
            mapOf(
                "Experience" to experience,
                "Frequency" to careFrequency,
                "Breed" to breedPreference
            )
        ).addOnSuccessListener {
            onPreferencesSaved()
        }.addOnFailureListener {
            // Handle failure (e.g., show a message)
        }
    }

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
    }
}
