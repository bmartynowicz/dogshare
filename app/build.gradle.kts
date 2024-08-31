plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.dogshare"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dogshare"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))

    // AndroidX and Jetpack Compose dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose UI and Material 3
    implementation("androidx.compose.ui:ui:1.6.8")
    implementation("androidx.compose.foundation:foundation:1.6.8")
    implementation("androidx.compose.material:material:1.6.8")
    implementation("androidx.compose.material3:material3:1.2.1")

    // Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation("com.google.firebase:firebase-auth-ktx") // For Firebase Authentication
    implementation("com.google.firebase:firebase-firestore-ktx") // If you're using Firestore

    // Navigation for Compose
    implementation("androidx.navigation:navigation-compose:2.7.1")

    // Material Icons and Accompanist
    implementation("androidx.compose.material:material-icons-core:1.6.8")
    implementation("androidx.compose.material:material-icons-extended:1.6.8")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.34.0")

    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.2.2")

    // Google Play Services for location
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation(libs.androidx.media3.common)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.8")

    // Debugging tools
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.8")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.8")
}
