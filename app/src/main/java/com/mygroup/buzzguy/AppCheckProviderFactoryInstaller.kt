package com.mygroup.buzzguy

import com.google.firebase.appcheck.FirebaseAppCheck

interface AppCheckProviderFactoryInstaller {
    fun install(firebaseAppCheck: FirebaseAppCheck)
}