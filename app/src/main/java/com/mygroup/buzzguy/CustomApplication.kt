package com.mygroup.buzzguy

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import androidx.core.content.edit

class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("AppCheckSetup", "CustomApplication onCreate started.")

        // Initialize Firebase first
        FirebaseApp.initializeApp(this)
        Log.d("AppCheckSetup", "FirebaseApp initialized.")
        val firebaseAppCheck = FirebaseAppCheck.getInstance()

        // This works for both your running app AND your instrumented tests.
        if (BuildConfig.DEBUG) {
            val debugToken = BuildConfig.FIREBASE_APP_CHECK_DEBUG_TOKEN
            if (debugToken.isNotEmpty()) {
                Log.d("AppCheckSetup", "Writing persistent debug token to storage.")

                // This is a required workaround to force the SDK to use a specific debug token.
                // It writes the token to the exact Shared Preferences key the SDK expects to find it in.
                val prefs = applicationContext.getSharedPreferences(
                    "com.google.firebase.appcheck.debug.store." + FirebaseApp.getInstance().persistenceKey,
                    Context.MODE_PRIVATE
                )
                prefs.edit {
                    putString(
                        "com.google.firebase.appcheck.debug.DEBUG_SECRET",
                        debugToken
                    ).apply()
                }
            }

            Log.d("AppCheckSetup", "Build is DEBUG. Installing DebugAppCheckProviderFactory.")
            firebaseAppCheck.installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance()
            )
        } else {
            Log.d("AppCheckSetup", "Build is RELEASE. Installing PlayIntegrityAppCheckProviderFactory.")
            firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance()
            )
        }
        Log.d("AppCheckSetup", "App Check provider installed.")
    }
}