package com.dogshare.network

import com.dogshare.api.UnsplashPhoto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {
    @GET("photos/random")
    fun getRandomDogPhotos(
        @Query("client_id") accessKey: String,
        @Query("query") query: String = "dog",
        @Query("count") count: Int = 1
    ): Call<List<UnsplashPhoto>>
}
