package com.dogshare.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.tasks.await

class AuthService(private val auth: FirebaseAuth) {

    suspend fun signIn(email: String, password: String): Result<String> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(auth.currentUser?.uid ?: "Unknown User ID")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createAccount(email: String, password: String): Result<String> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(auth.currentUser?.uid ?: "Unknown User ID")
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(Exception("Account already exists with a different password."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}