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


# ==== ML Kit (vision / objetos) ====
-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**

# GMS internos usados por ML Kit (evita warnings)
-keep class com.google.android.gms.internal.mlkit_vision_** { *; }
-dontwarn com.google.android.gms.internal.mlkit_vision_**

# CameraX
-keep class androidx.camera.** { *; }
-dontwarn androidx.camera.**

# Tu app (no obfusques nombres públicos si accedes por reflexión)
-keep class com.espe.aprendesenasv1.** { *; }

# Mantener anotaciones/reflexión
-keepattributes *Annotation*
