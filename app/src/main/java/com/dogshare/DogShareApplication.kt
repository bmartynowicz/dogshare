package com.dogshare

import android.app.Application
import com.dogshare.di.appModule
import com.dogshare.di.viewModelModule
import com.dogshare.utils.PreferencesManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DogShareApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize PreferencesManager with the application context
        PreferencesManager.initialize(this)

        // Start Koin with the list of modules that you've defined
        startKoin {
            androidContext(this@DogShareApplication) // Set the Android context for Koin
            modules(listOf(appModule, viewModelModule)) // Replace with your own module list
        }
    }
}
