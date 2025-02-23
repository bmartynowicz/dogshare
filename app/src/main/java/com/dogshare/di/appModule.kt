package com.dogshare.di

import com.dogshare.repository.PreferencesRepository
import com.dogshare.repository.UserRepository
import com.dogshare.services.AuthService
import com.dogshare.viewmodels.CreateAccountViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }

    // PreferencesRepository definition
    single { PreferencesRepository(androidContext()) }

    // UserRepository definition (add this line)
    single { UserRepository() }

    // AuthService definition
    single { AuthService(get()) }

    // ViewModels
    viewModel { CreateAccountViewModel(get(), get()) }
}
