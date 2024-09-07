// appModule.kt
package com.dogshare.di

import com.dogshare.repository.PreferencesRepository
import com.dogshare.services.AuthService
import com.dogshare.viewmodels.CreateAccountViewModel
import com.dogshare.viewmodels.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { PreferencesRepository(get(), get()) }
    single { AuthService(get()) } // Provide AuthService

    viewModel { CreateAccountViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) } // New LoginViewModel
}