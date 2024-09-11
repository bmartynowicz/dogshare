package com.dogshare.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.dogshare.navigation.AppNavigation
import com.dogshare.navigation.NavigationRoutes
import com.dogshare.repository.PreferencesRepository
import com.dogshare.ui.theme.DogShareTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import com.dogshare.R

class MainActivity : ComponentActivity() {
    private val preferencesRepository: PreferencesRepository by inject()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        setContent {
            DogShareTheme {
                DogShareApp(preferencesRepository, onGoogleSignIn = { startGoogleSignIn() })
            }
        }
    }

    // Set up Google Sign-In
    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.let { firebaseAuthWithGoogle(it) }
        } catch (e: ApiException) {
            Log.e("MainActivity", "Google sign-in failed", e)
        }
    }

    private fun startGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val signInClient = GoogleSignIn.getClient(this, gso)
        signInLauncher.launch(signInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        lifecycleScope.launch {
            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        Log.d("MainActivity", "Sign-in successful: ${it.displayName}")
                        preferencesRepository.setUserId(user.uid)
                        preferencesRepository.updateLastLoginTimestamp()
                    }
                } else {
                    Log.e("MainActivity", "Authentication failed", task.exception)
                }
            }
        }
    }
}

@Composable
fun DogShareApp(preferencesRepository: PreferencesRepository, onGoogleSignIn: () -> Unit) {
    val navController = rememberNavController()
    var startDestination by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    // Perform the login check in a coroutine to avoid blocking the UI
    LaunchedEffect(Unit) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val storedUserId = preferencesRepository.getUserId()

        Log.d("MainActivity", "Stored userId: $storedUserId")

        if (firebaseUser != null && storedUserId != null) {
            startDestination = NavigationRoutes.LandingPage.createRoute(storedUserId)
        } else {
            startDestination = NavigationRoutes.Login.route
        }

        isLoading = false
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        startDestination?.let {
            AppNavigation(navController = navController, startDestination = it, onGoogleSignIn = onGoogleSignIn)
        }
    }
}
