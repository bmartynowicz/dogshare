package com.dogshare.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await

class AuthService(private val auth: FirebaseAuth) {

    suspend fun signIn(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: throw IllegalStateException("User ID not found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createAccount(email: String, password: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: throw IllegalStateException("User ID not found"))
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(Exception("Account already exists with a different credential."))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithGoogle(idToken: String): Result<String> {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return try {
            auth.signInWithCredential(credential).await()
            Result.success(auth.currentUser?.uid ?: "Unknown User ID")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isSignedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}
