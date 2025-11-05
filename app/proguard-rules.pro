# These rules are necessary because Ktor uses reflection, which fools R8.
-keep class io.ktor.client.** { *; }
-keep class io.ktor.client.engine.cio.** { *; }
-keep class io.ktor.client.plugins.contentnegotiation.** { *; }
-keep class io.ktor.client.plugins.logging.** { *; }
-keep class io.ktor.client.plugins.auth.** { *; }
-keep class io.ktor.client.plugins.api.** { *; }
-keep class io.ktor.client.request.** { *; }
-keep class io.ktor.client.statement.** { *; }
-keep class io.ktor.client.utils.** { *; }
-keep class io.ktor.http.** { *; }
-keep class io.ktor.http.cio.** { *; }
-keep class io.ktor.util.** { *; }
-keep class io.ktor.util.date.** { *; }
-keep class io.ktor.network.** { *; }
-keep class io.ktor.network.sockets.** { *; }
-keep class io.ktor.network.tls.** { *; }
-keep class io.ktor.serialization.** { *; }
-keep class io.ktor.serialization.kotlinx.** { *; }
-keep class io.ktor.serialization.kotlinx.json.** { *; }

# Also keep classes for the JSON serializer if you use it
-keep class kotlinx.serialization.** { *; }
-keep class kotlinx.serialization.json.** { *; }
-keep class kotlinx.serialization.internal.** { *; }

# Keep attributes for inner classes metadata
-keepattributes InnerClasses, Signature, EnclosingMethod

# This tells R8 not to worry about missing standard Java management classes,
# which are not part of the Android runtime. Ktor uses them for debug detection.
-dontwarn java.lang.management.**

# This is a general rule for libraries that use Kotlin Coroutines.
-dontwarn kotlinx.coroutines.flow.**

# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile