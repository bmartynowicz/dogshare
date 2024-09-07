package com.dogshare

import android.app.Application
import android.content.Context
import android.util.Log
import com.dogshare.di.appModule
import com.dogshare.di.viewModelModule
import com.dogshare.utils.PreferencesManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class DogShareApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Log.d("DogShareApp", "onCreate called - Initializing PreferencesManager and Koin")

        // Initialize PreferencesManager with the application context
        PreferencesManager.initialize(this)

        // Start Koin with the list of modules that you've defined
        startKoin {
            androidContext(this@DogShareApp) // Set the Android context for Koin
            modules(listOf(appModule, viewModelModule)) // Replace with your own module list
        }

        // Check and update the last login timestamp
        checkAndUpdateLoginTimestamp()
    }

    private fun checkAndUpdateLoginTimestamp() {
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val lastLoginTimestamp = sharedPreferences.getLong("last_login_timestamp", 0L)
        val currentTime = System.currentTimeMillis()

        val fifteenDaysInMillis = TimeUnit.DAYS.toMillis(15)
        Log.d("DogShareApp", "Checking last login timestamp...")

        if (currentTime - lastLoginTimestamp > fifteenDaysInMillis) {
            Log.d("DogShareApp", "More than 15 days since last login, setting prompt_login to true")
            // If more than 15 days have passed, prompt for login by setting a flag
            sharedPreferences.edit().putBoolean("prompt_login", true).apply()
        } else {
            Log.d("DogShareApp", "Within 15 days since last login, updating last login timestamp")
            // Update the last login timestamp
            sharedPreferences.edit().putLong("last_login_timestamp", currentTime).apply()
            // Ensure the prompt_login flag is false
            sharedPreferences.edit().putBoolean("prompt_login", false).apply()
        }
    }
}
