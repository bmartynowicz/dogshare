package com.dogshare.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dogshare.network.RetrofitClient
import com.dogshare.api.UnsplashPhoto
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SwipingScreenViewModel : ViewModel() {
    var dogPhotos by mutableStateOf<List<UnsplashPhoto>>(emptyList())

    fun fetchDogPhotos(accessKey: String) {
        viewModelScope.launch {
            val api = RetrofitClient.getClient()
            api.getRandomDogPhotos(accessKey).enqueue(object : Callback<List<UnsplashPhoto>> {
                override fun onResponse(call: Call<List<UnsplashPhoto>>, response: Response<List<UnsplashPhoto>>) {
                    if (response.isSuccessful) {
                        dogPhotos = response.body() ?: emptyList()
                    }
                }

                override fun onFailure(call: Call<List<UnsplashPhoto>>, t: Throwable) {
                    // Handle error
                }
            })
        }
    }
}
