package com.mygroup.buzzguy

import android.content.Context

object AppCheckInstallerFactory {
    // The create function now requires a Context
    fun create(context: Context): AppCheckInstaller {
        return ReleaseAppCheckInstaller()
    }
}