# Broad rules because Ktor uses reflection extensively.
-keep class io.ktor.** { *; }
-keep class io.ktor.client.engine.cio.** { *; }

# Rules added to fix R8 build warnings/errors (from CI/CD log).
# The -keep rules prevent removal, and -dontwarn suppresses missing class warnings.
-keep class io.ktor.client.plugins.HttpTimeout { *; }
-keep class io.ktor.client.plugins.HttpTimeout$Plugin { *; }
-keep class io.ktor.client.plugins.HttpTimeout$HttpTimeoutCapabilityConfiguration { *; }
-dontwarn io.ktor.client.plugins.HttpTimeout
-dontwarn io.ktor.client.plugins.HttpTimeout$Plugin
-dontwarn io.ktor.client.plugins.HttpTimeout$HttpTimeoutCapabilityConfiguration

-keep class io.ktor.client.plugins.contentnegotiation.ContentNegotiation { *; }
-keep class io.ktor.client.plugins.contentnegotiation.ContentNegotiation$Plugin { *; }
-keep class io.ktor.client.plugins.contentnegotiation.ContentNegotiation$Config { *; }
-dontwarn io.ktor.client.plugins.contentnegotiation.ContentNegotiation
-dontwarn io.ktor.client.plugins.contentnegotiation.ContentNegotiation$Plugin
-dontwarn io.ktor.client.plugins.contentnegotiation.ContentNegotiation$Config

# Rule for Netty, a dependency of the CIO engine.
-dontwarn io.netty.**

# Rule for Ktor debug detection, which uses classes not in Android.
-dontwarn java.lang.management.**

# Rules for kotlinx.serialization
# This keeps the metadata required for reflection-based serialization.
-keepattributes *Annotation*

# Keep all classes that are annotated with @Serializable or @Serializer,
# and also keep their companion objects, which hold the serializer().
# The '*' wildcard covers all packages, including your 'com.mygroup.buzzguy.data.model'.
-keep,includedescriptorclasses class * {
    @kotlinx.serialization.Serializable <methods>;
    @kotlinx.serialization.Serializer <methods>;
}
# Keep the constructors of all Companion objects, which is a reliable way to
# prevent them from being removed by R8. This is crucial for runtime serialization.
-keepclassmembers class **$Companion {
    <init>(...);
}
-keep,includedescriptorclasses class com.mygroup.buzzguy.data.model.**$$serializer

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