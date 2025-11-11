package com.mygroup.buzzguy

import com.google.firebase.appcheck.FirebaseAppCheck

interface AppCheckInstaller {
    fun install(firebaseAppCheck: FirebaseAppCheck)
}