package com.dogshare.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    // Check if the user is authenticated and session is not expired
    fun isUserAuthenticated(): Boolean {
        val user = firebaseAuth.currentUser
        return user != null && !isSessionExpired(user)
    }

    private fun isSessionExpired(user: FirebaseUser): Boolean {
        val lastSignInTimestamp = user.metadata?.lastSignInTimestamp ?: return true
        val currentTime = System.currentTimeMillis()
        val daysSinceLastLogin = (currentTime - lastSignInTimestamp) / (1000 * 60 * 60 * 24)

        // Assume session expires if the user hasn't logged in within 30 days
        return daysSinceLastLogin > 30
    }

    fun signOutUser() {
        firebaseAuth.signOut()
    }

    // Auto sign-in logic: Check if token is valid, refresh token if necessary
    fun signInAutomatically(onSuccess: (FirebaseUser) -> Unit, onFailure: () -> Unit) {
        val currentUser = firebaseAuth.currentUser
        currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Token is valid, return the current user
                onSuccess(currentUser)
            } else {
                // Token is invalid or session expired, force sign out
                signOutUser()
                onFailure()
            }
        } ?: onFailure()
    }
}
