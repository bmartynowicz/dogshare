// File: viewModelModule.kt
package com.dogshare.di

import com.dogshare.repository.PreferencesRepository
import com.dogshare.viewmodels.MainViewModel
import com.dogshare.viewmodels.SettingsViewModel
import com.dogshare.viewmodels.ProfileViewModel
import com.dogshare.viewmodels.LogoutViewModel
import com.dogshare.viewmodels.CreateAccountViewModel
import com.dogshare.viewmodels.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    // Provide FirebaseAuth instance
    single { FirebaseAuth.getInstance() }

    // Provide Firebase Firestore instance
    single { FirebaseFirestore.getInstance() }

    // Provide PreferencesRepository with Context and Firebase Firestore as dependencies
    single { PreferencesRepository(get())}

    // Provide MainViewModel with FirebaseAuth as a dependency
    viewModel { MainViewModel(get()) }

    // Provide SettingsViewModel with PreferencesRepository as a dependency
    viewModel { SettingsViewModel(get()) }

    // Provide ProfileViewModel for ProfileScreen
    viewModel { ProfileViewModel() }

    // Provide LogoutViewModel for handling logout
    viewModel { LogoutViewModel(get()) }

    // Provide CreateAccountViewModel with PreferencesRepository and FirebaseAuth as dependencies
    viewModel { CreateAccountViewModel(get(), get()) }

    viewModel { LoginViewModel(get(), get(), get()) }
}