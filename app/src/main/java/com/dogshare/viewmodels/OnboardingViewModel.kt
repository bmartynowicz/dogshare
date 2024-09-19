package com.dogshare.viewmodels

import androidx.lifecycle.ViewModel
import com.dogshare.repository.PreferencesRepository

class OnboardingViewModel(private val preferencesRepository: PreferencesRepository) : ViewModel() {

    fun saveBreedPreference(breed: String) {
        preferencesRepository.setBreedPreference(breed)
    }

    fun saveLocationSettings(radius: Int) {
        preferencesRepository.setLocationRadius(radius)
    }

    fun saveAvailability(availability: String) {
        preferencesRepository.setAvailability(availability)
    }

    fun saveActivityLevel(activityLevel: String) {
        preferencesRepository.setActivityLevel(activityLevel)
    }

    fun completeOnboarding() {
        // Assuming that the user ID has been set and retrieved correctly
        preferencesRepository.getUserId()?.let { userId ->
            preferencesRepository.syncPreferencesToCloud(userId)
        }
        // Optionally handle the case where userId is null and ensure it is managed appropriately
    }
}
