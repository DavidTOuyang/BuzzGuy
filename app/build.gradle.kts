import java.util.concurrent.TimeUnit

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.kotlin.parcelize)
}

// Helper function to execute a command and return its output.
// Mainly used for version controls.
fun String.runCommand(): String {
    return try {
        val parts = this.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray())
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
        proc.waitFor(60, TimeUnit.SECONDS)
        proc.inputStream.bufferedReader().readText().trim()
    } catch (e: java.io.IOException) {
        // Return a default value if git isn't available or the command fails
        e.printStackTrace()
        ""
    }
}

// Function to get the versionCode from the total number of Git commits.
fun getVersionCodeFromGit(): Int {
    val commitCount = "git rev-list --count HEAD".runCommand()
    // Fallback to 1 if the Git command fails
    return if (commitCount.isNotEmpty()) commitCount.toInt() else 1
}

// Function to get the versionName from the latest Git tag.
fun getVersionNameFromGit(): String {
    val versionName = "git describe --tags --abbrev=0".runCommand()
    // Fallback to "1.0" if no tags are found
    return versionName.ifEmpty { "1.0" }
}

// Read the email from an environment variable or use a default
val contactEmail = System.getenv("CONTACT_EMAIL") ?: "local.build@example.com"

android {
    namespace = "com.example.myapplication"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 36
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        versionCode = getVersionCodeFromGit()
        versionName = getVersionNameFromGit()
        println("Building version '$versionName' (code $versionCode)")
        resValue("string", "contact_email", "\"$contactEmail\"")
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.crashlytics)
    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))

    // Add the specific dependencies using aliases
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.analytics)
    implementation(libs.google.material)
    implementation(libs.firebase.ai)
    implementation(libs.generativeai)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.rules)
    implementation(libs.androidx.core)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.uiautomator)

}