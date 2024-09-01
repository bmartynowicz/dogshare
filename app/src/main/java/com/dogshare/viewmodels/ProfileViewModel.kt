package com.dogshare.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // States for profile fields
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _petType = MutableStateFlow("")
    val petType: StateFlow<String> = _petType

    private val _petSize = MutableStateFlow("")
    val petSize: StateFlow<String> = _petSize

    private val _livingCondition = MutableStateFlow("")
    val livingCondition: StateFlow<String> = _livingCondition

    private val _activityLevel = MutableStateFlow("")
    val activityLevel: StateFlow<String> = _activityLevel

    private val _travelDistance = MutableStateFlow("")
    val travelDistance: StateFlow<String> = _travelDistance

    private val _saveProfileState = MutableStateFlow("")
    val saveProfileState: StateFlow<String> = _saveProfileState

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Fetch profile data from Firestore
    fun fetchProfile(userId: String) {
        _isLoading.value = true
        db.collection("profiles").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    _email.value = document.getString("email") ?: ""
                    _petType.value = document.getString("petType") ?: ""
                    _petSize.value = document.getString("petSize") ?: ""
                    _livingCondition.value = document.getString("livingCondition") ?: ""
                    _activityLevel.value = document.getString("activityLevel") ?: ""
                    _travelDistance.value = document.getString("travelDistance") ?: ""
                }
                _isLoading.value = false
            }
            .addOnFailureListener { e ->
                Log.e("ProfileViewModel", "Error fetching profile: ${e.message}")
                _saveProfileState.value = "Error fetching profile: ${e.message}"
                _isLoading.value = false
            }
    }

    // Save profile data to Firestore
    fun saveProfile(userId: String) {
        _isLoading.value = true
        val userProfile = mapOf(
            "email" to _email.value,
            "petType" to _petType.value,
            "petSize" to _petSize.value,
            "livingCondition" to _livingCondition.value,
            "activityLevel" to _activityLevel.value,
            "travelDistance" to _travelDistance.value
        )
        db.collection("profiles").document(userId)
            .set(userProfile)
            .addOnSuccessListener {
                _saveProfileState.value = "Profile Updated Successfully"
                _isLoading.value = false
            }
            .addOnFailureListener { e ->
                Log.e("ProfileViewModel", "Error updating profile: ${e.message}")
                _saveProfileState.value = "Error updating profile: ${e.message}"
                _isLoading.value = false
            }
    }

    // Functions to update individual fields
    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePetType(newPetType: String) {
        _petType.value = newPetType
    }

    fun updatePetSize(newPetSize: String) {
        _petSize.value = newPetSize
    }

    fun updateLivingCondition(newLivingCondition: String) {
        _livingCondition.value = newLivingCondition
    }

    fun updateActivityLevel(newActivityLevel: String) {
        _activityLevel.value = newActivityLevel
    }

    fun updateTravelDistance(newTravelDistance: String) {
        _travelDistance.value = newTravelDistance
    }

    // Function to handle logout
    fun onLogout() {
        // Perform any necessary cleanup or state updates on logout
    }
}
