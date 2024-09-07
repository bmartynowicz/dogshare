package com.dogshare

import android.app.Application
import android.util.Log
import com.dogshare.di.appModule
import com.dogshare.di.viewModelModule
import com.dogshare.repository.PreferencesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject

class DogShareApp : Application() {

    // Inject PreferencesRepository using Koin
    private val preferencesRepository: PreferencesRepository by inject(PreferencesRepository::class.java)

    override fun onCreate() {
        super.onCreate()

        Log.d("DogShareApp", "onCreate called - Initializing PreferencesManager and Koin")

        // Start Koin with the list of modules that you've defined
        startKoin {
            androidContext(this@DogShareApp) // Set the Android context for Koin
            modules(listOf(appModule, viewModelModule)) // Replace with your own module list
        }

        // Check and update the last login timestamp
        checkAndUpdateLoginTimestamp()
    }

    private fun checkAndUpdateLoginTimestamp() {
        val lastLoginTimestamp = preferencesRepository.getLastLoginTimestamp()
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastLoginTimestamp > 15 * 24 * 60 * 60 * 1000L) { // 15 days in millis
            preferencesRepository.setPromptLogin(true)
        } else {
            preferencesRepository.updateLastLoginTimestamp()
            preferencesRepository.setPromptLogin(false)
        }
    }
}