package com.mygroup.buzzguy

import android.content.Context

object AppCheckInstallerFactory {
    // The create function now requires a Context
    fun create(context: Context): AppCheckInstaller {
        // Pass the context to the constructor
        return DebugAppCheckInstaller(context)
    }
}
