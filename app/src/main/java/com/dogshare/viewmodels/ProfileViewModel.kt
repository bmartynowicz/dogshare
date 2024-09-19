package com.dogshare.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    // States for profile fields
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _breedPreference = MutableStateFlow("")
    val breedPreference: StateFlow<String> = _breedPreference

    private val _sizePreference = MutableStateFlow("")
    val sizePreference: StateFlow<String> = _sizePreference

    private val _livingCondition = MutableStateFlow("")
    val livingCondition: StateFlow<String> = _livingCondition

    private val _availability = MutableStateFlow("")
    val availability: StateFlow<String> = _availability

    private val _travelDistance = MutableStateFlow("")
    val travelDistance: StateFlow<String> = _travelDistance

    private val _saveProfileState = MutableStateFlow("")
    val saveProfileState: StateFlow<String> = _saveProfileState

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Settings states
    private val _notificationEnabled = MutableStateFlow(false)
    val notificationEnabled: StateFlow<Boolean> = _notificationEnabled

    private val _darkModeEnabled = MutableStateFlow(false)
    val darkModeEnabled: StateFlow<Boolean> = _darkModeEnabled

    private val _isDeletingAccount = MutableStateFlow(false)
    val isDeletingAccount: StateFlow<Boolean> = _isDeletingAccount

    // Fetch profile and settings data from Firestore
    fun fetchProfile(userId: String) {
        _isLoading.value = true
        Log.d("ProfileViewModel", "Fetching profile for userId: $userId")
        db.collection("profiles").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    _username.value = document.getString("username") ?: ""
                    _email.value = document.getString("email") ?: ""
                    _breedPreference.value = document.getString("breedPreference") ?: ""
                    _sizePreference.value = document.getString("sizePreference") ?: ""
                    _availability.value = document.getString("availability") ?: ""
                    _travelDistance.value = document.getString("travelDistance") ?: ""
                }
                _isLoading.value = false
            }
            .addOnFailureListener { e ->
                Log.e("ProfileViewModel", "Error fetching profile: ${e.message}")
                _isLoading.value = false
            }
    }


    // Save both profile and settings data to Firestore
    fun saveProfile(userId: String) {
        _isLoading.value = true

        // Combine profile and settings data into one map
        val userProfile = mapOf(
            "email" to _email.value,
            "breed" to _breedPreference.value,
            "petSize" to _sizePreference.value,
            "availability" to _availability.value,
            "livingCondition" to _livingCondition.value,
            "travelDistance" to _travelDistance.value,
            "notificationEnabled" to _notificationEnabled.value,
            "darkModeEnabled" to _darkModeEnabled.value
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

    // Update functions for settings fields
    fun updateNotificationEnabled(enabled: Boolean) {
        _notificationEnabled.value = enabled
    }

    fun updateDarkModeEnabled(enabled: Boolean) {
        _darkModeEnabled.value = enabled
    }

    // Function to delete account
    fun deleteAccount(userId: String) {
        _isDeletingAccount.value = true
        // Delete user data from Firestore
        db.collection("profiles").document(userId).delete()
            .addOnSuccessListener {
                val user = FirebaseAuth.getInstance().currentUser
                user?.delete()?.addOnCompleteListener { task ->
                    _isDeletingAccount.value = false
                    if (task.isSuccessful) {
                        Log.d("ProfileViewModel", "User account deleted.")
                    } else {
                        Log.e("ProfileViewModel", "Error deleting user account: ${task.exception?.message}")
                    }
                }
            }
            .addOnFailureListener { e ->
                _isDeletingAccount.value = false
                Log.e("ProfileViewModel", "Error deleting account: ${e.message}")
            }
    }
}
