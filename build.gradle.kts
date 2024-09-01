// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        // Classpath for Google Services, adjust versions as necessary
        classpath("com.google.gms:google-services:4.4.2")
        // Add any additional classpath dependencies needed for your build tools here
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

// Removed the allprojects block containing repositories

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}
