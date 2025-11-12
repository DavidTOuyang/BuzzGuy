package com.mygroup.buzzguy

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("AppCheckSetup", "CustomApplication onCreate started.")

        // Initialize Firebase first
        FirebaseApp.initializeApp(this)
        Log.d("AppCheckSetup", "FirebaseApp initialized.")
        val firebaseAppCheck = FirebaseAppCheck.getInstance()

        // Use the factory to get the correct installer for the current build type.
        // Pass 'this' (the Application Context) to the create method.
        val installer = AppCheckInstallerFactory.create(this)

        installer.install(firebaseAppCheck)
        Log.d("AppCheckSetup", "App Check provider installed.")
    }
}