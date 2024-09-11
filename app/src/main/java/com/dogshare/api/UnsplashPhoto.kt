package com.dogshare.api

data class UnsplashPhoto(
    val id: String,
    val urls: Urls
)

data class Urls(
    val regular: String
)
