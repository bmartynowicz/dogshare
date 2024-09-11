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
    implementation(libs.androidx.security.crypto.ktx.v110alpha06)

    // Squareup API dependencies for image retrival
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // AndroidX and Jetpack Compose dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose UI and Material 3
    implementation(libs.ui)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.material)
    implementation(libs.material3)

    // Firebase dependencies
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)

    // Koin for dependency injection framework for Kotlin
    implementation(libs.koin.core.v320)
    implementation(libs.koin.android.v320)
    implementation(libs.koin.androidx.compose.v320) // Use this for Jetpack Compose

    // Navigation for Compose
    implementation(libs.androidx.navigation.compose)

    // Material Icons and Accompanist
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.accompanist.navigation.animation)
    implementation("com.google.android.material:material:1.9.0")

    // Coil for image loading
    implementation(libs.coil.compose)

    // Google Play Services for location
    implementation(libs.play.services.location)
    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.security.crypto.ktx)
    implementation(libs.androidx.room.paging)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)

    // Debugging tools
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}