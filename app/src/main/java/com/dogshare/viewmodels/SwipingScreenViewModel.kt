package com.dogshare.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dogshare.api.UnsplashPhoto
import com.dogshare.network.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import com.dogshare.models.DogProfile

class SwipingScreenViewModel : ViewModel() {
    var dogProfiles by mutableStateOf<List<DogProfile>>(emptyList())  // List of DogProfile objects
    var currentPhotoIndex by mutableStateOf(0)
    var errorMessage by mutableStateOf<String?>(null)

    // Sample dog data
    private val dogNames = listOf("Max", "Bella", "Charlie", "Luna", "Rocky")
    private val dogBreeds = listOf("Golden Retriever", "Bulldog", "Poodle", "Beagle", "Shiba Inu")
    private val dogAges = listOf(2, 3, 4, 5, 6)

    // Fetch dog profiles and link each one to a photo from Unsplash
    fun fetchDogProfiles(accessKey: String) {
        viewModelScope.launch {
            try {
                val api = RetrofitClient.getClient()
                val response = api.getRandomDogPhotos(accessKey)
                if (response.isSuccessful) {
                    val photos = response.body() ?: emptyList()
                    dogProfiles = photos.mapIndexed { index, unsplashPhoto ->
                        DogProfile(
                            name = dogNames[index % dogNames.size],
                            age = dogAges.random(),
                            breed = dogBreeds.random(),
                            imageUrl = unsplashPhoto.urls.regular
                        )
                    }
                    Log.i("SwipingScreenViewModel", "Dog profiles loaded: ${dogProfiles.size}")
                } else {
                    errorMessage = "Failed to fetch photos: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = "An error occurred: ${e.message}"
            }
        }
    }


    // Update the current photo index for swiping
    fun updatePhotoIndex() {
        if (dogProfiles.isNotEmpty()) {
            currentPhotoIndex = (currentPhotoIndex + 1) % dogProfiles.size
            Log.i("SwipingScreenViewModel", "New currentPhotoIndex: $currentPhotoIndex")
            // Pre-fetch more profiles if near the end of the list
            if (currentPhotoIndex > dogProfiles.size - 2) {  // Adjust threshold as needed
                fetchDogProfiles("nNC9XgA77_sgn-co85RYbEaawFa-zopkHRokr7HkSN0")
            }
        } else {
            Log.i("SwipingScreenViewModel", "No photos to display.")
        }
    }

}
