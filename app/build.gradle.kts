import java.util.concurrent.TimeUnit
import java.util.Properties

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
fun getVersionCodeFallback(): Int {
    val commitCount = "git rev-list --count HEAD".runCommand()
    // Fallback to 1 if the Git command fails
    return if (commitCount.isNotEmpty()) commitCount.toInt() else 1
}

// Function to get the versionName from the latest Git tag.
fun getVersionNameFromGit(): String {
    val versionName = "git describe --tags --abbrev=0".runCommand()
    // Fallback to "1.0" if no tags are found
    return versionName.ifEmpty { "1.0-local" }
}

// Read the email from an environment variable or use a default
val contactEmail = System.getenv("CONTACT_EMAIL") ?: "local.build@mygroup.com"

android {
    // Function to read the properties file
    fun getFirebaseDebugToken(): String? {
        val envToken = System.getenv("FIREBASE_DEBUG_TOKEN")
        if (!envToken.isNullOrEmpty()) {
            return envToken
        }
        val properties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { input ->
                properties.load(input)
            }
        }
        return properties.getProperty("firebase.debug.token", "")
    }


    namespace = "com.mygroup.buzzguy"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.mygroup.buzzguy"
        minSdk = 24
        targetSdk = 36
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        versionCode = project.findProperty("appVersionCode")?.toString()?.toInt() ?: getVersionCodeFallback()
        versionName = project.findProperty("appVersionName") as? String ?: getVersionNameFromGit()
        println("Building version '$versionName' (code $versionCode)")
        resValue("string", "contact_email", "\"$contactEmail\"")
    }
    buildTypes {
        debug {
            // Read the token and inject it into the BuildConfig class
            buildConfigField(
                "String",
                "FIREBASE_APP_CHECK_DEBUG_TOKEN",
                "\"${getFirebaseDebugToken()?.trim()}\""
            )
        }
        release {
            buildConfigField(
                "String",
                "FIREBASE_APP_CHECK_DEBUG_TOKEN",
                "\"\""
            )
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // This tells Gradle to generate the native debug symbols for the release build.
            ndk.debugSymbolLevel = "FULL"
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
        buildConfig = true
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
    implementation(libs.firebase.appcheck.playintegrity)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.uiautomator)
    debugImplementation(libs.firebase.appcheck.debug)
}