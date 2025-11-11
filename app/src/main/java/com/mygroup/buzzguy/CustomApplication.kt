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

        if (BuildConfig.DEBUG) {
            // This line links to the DebugAppCheckInstaller class in the debug source set
            DebugAppCheckInstaller(applicationContext).install(firebaseAppCheck)
        } else {
            // This uses the release provider, which is always available in the main source set
            Log.d(
                "AppCheckSetup",
                "Build is RELEASE. Installing PlayIntegrityAppCheckProviderFactory."
            )
            firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance()
            )
        }
        Log.d("AppCheckSetup", "App Check provider installed.")
    }
}