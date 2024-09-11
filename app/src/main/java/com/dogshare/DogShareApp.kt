package com.dogshare

import android.app.Application
import android.util.Log
import com.dogshare.di.appModule
import com.dogshare.di.viewModelModule
import com.dogshare.repository.PreferencesRepository
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class DogShareApp : Application() {

    private lateinit var preferencesRepository: PreferencesRepository

    override fun onCreate() {
        super.onCreate()
        initializeServices()
    }

    private fun initializeServices() {
        // Initialize Koin
        startKoin {
            androidContext(this@DogShareApp)
            modules(listOf(appModule, viewModelModule))
        }

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Setup Preferences
        setupPreferences()
    }

    private fun setupPreferences() {
        preferencesRepository = GlobalContext.get().get<PreferencesRepository>()
        checkAndUpdateLoginTimestamp()
    }

    private fun checkAndUpdateLoginTimestamp() {
        val lastLoginTimestamp = preferencesRepository.getLastLoginTimestamp()
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastLoginTimestamp > 15 * 24 * 60 * 60 * 1000L) {
            preferencesRepository.setPromptLogin(true)
        } else {
            preferencesRepository.updateLastLoginTimestamp()
            preferencesRepository.setPromptLogin(false)
        }
    }
}

