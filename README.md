# dogshare
DogShare Application
DogShare is a mobile application designed for Android, primarily programmed in Java with components in Kotlin. The app is built with the goal of providing a platform for dog owners to share information, preferences, and connect with each other. It leverages Firebase for authentication, Firestore for data storage, and Koin for dependency injection. The app is designed to be user-friendly and is equipped with modern Android features, including Jetpack Compose for UI and a robust navigation system.

Features
User Authentication: Firebase Authentication is used to manage user sign-in, sign-out, and user status.
Profile Management: Users can create and update their profiles, including preferences related to their dogs.
Settings Management: Users can manage app settings, such as notifications, dark mode, and account privacy, which are stored in Firebase Firestore.
Navigation: The app uses a navigation structure built with Jetpack Compose, enabling seamless movement between screens.
Dependency Injection: Koin is used for dependency injection, ensuring clean and manageable code.
Firebase Integration: The app integrates Firebase services for authentication, database, and Firestore operations.
Android Location Services: The app uses Google Play Services for location-based functionalities.
Image Loading: Coil is used for efficient image loading within the app.

Project Structure
Main Application: DogShareApplication initializes the application and Koin modules.
Main Activity: MainActivity drives the primary activities and UI of the application.
Modules: The project includes appModule and viewModelModule for managing dependencies with Koin.
Repositories: Includes PreferencesRepository and UserRepository for managing data-related operations.
Utilities: Utilities like AuthUtils, checkUserPreferences, FirestoreUtils, PermissionUtils, and PreferencesManager help manage various functions across the app.
Installation

Clone the repository:
bash
Copy code
git clone https://github.com/bmartynowicz/dogshare.git
Open the project in Android Studio.

Set up Firebase:

Add your google-services.json to the app directory.
Ensure Firebase is set up in your project by following the Firebase setup guide.
Build and Run:

Sync your project with Gradle files.
Build the project.
Run the app on your emulator or Android device.
Requirements
Android SDK: Minimum SDK version 29, Target SDK version 33 or higher.
Build Tools: Android Studio with Gradle support.
Firebase Account: Required for authentication and Firestore operations.
Google Play Services: Necessary for location-based services.
Contribution
If you would like to contribute to this project:

Fork the repository.
Create a new feature branch (git checkout -b feature-branch).
Commit your changes (git commit -am 'Add new feature').
Push to the branch (git push origin feature-branch).
Create a new Pull Request.
License
This project is licensed under the GPL-3.0 license file for details.

Contact
For more information, feel free to reach out via:

Email: bmartynowicz@gmail.com
GitHub: bmartynowicz