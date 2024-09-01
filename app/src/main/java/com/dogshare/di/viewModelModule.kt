package com.dogshare.di

import com.dogshare.repository.PreferencesRepository
import com.dogshare.viewmodels.MainViewModel
import com.dogshare.viewmodels.SettingsViewModel
import com.dogshare.viewmodels.ProfileViewModel // Import ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    // Provide FirebaseAuth instance
    single { FirebaseAuth.getInstance() }

    // Provide PreferencesRepository
    single { PreferencesRepository(get()) }

    // Provide MainViewModel with FirebaseAuth as a dependency
    viewModel { MainViewModel(get()) }

    // Provide SettingsViewModel with PreferencesRepository as a dependency
    viewModel { SettingsViewModel(get()) }

    // Provide ProfileViewModel for ProfileScreen
    viewModel { ProfileViewModel() }
}
