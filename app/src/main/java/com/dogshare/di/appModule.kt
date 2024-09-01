// File: appModule.kt
package com.dogshare.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val appModule = module {
    // Define your singleton dependencies here
    // For example, if you're using Firebase:
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
}
