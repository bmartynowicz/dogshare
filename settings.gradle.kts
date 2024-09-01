pluginManagement {
    repositories {
        google {
            content {
                // These regex patterns ensure only necessary Google libraries are included
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()  // Google's Maven repository, necessary for Android dependencies
        mavenCentral()  // Maven Central for all other open-source dependencies
        // Ensure all required repositories are listed here
    }
}

rootProject.name = "Dog Share"
include(":app")  // Including the main app module, add other modules if present
