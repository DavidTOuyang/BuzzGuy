package com.mygroup.buzzguy

import android.util.Log
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

class ReleaseAppCheckInstaller : AppCheckInstaller {
    override fun install(firebaseAppCheck: FirebaseAppCheck) {
        Log.d("AppCheckSetup", "ReleaseAppInstaller: Using Play Integrity Provider.")
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )
    }
}
