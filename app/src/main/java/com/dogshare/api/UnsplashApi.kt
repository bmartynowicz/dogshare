package com.dogshare.network

import com.dogshare.api.UnsplashPhoto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {
    @GET("photos/random")
    suspend fun getRandomDogPhotos(
        @Query("client_id") accessKey: String,
        @Query("query") query: String = "dog",
        @Query("count") count: Int = 1
    ): Response<List<UnsplashPhoto>>  // Returning Response for coroutine support
}

