# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#-keep public class com.google.ads.**{ public *; }
#-keep public class com.google.android.gms.* { public *; }
#-dontwarn com.google.android.gms.**

#-dontwarn javax.management.**
#-dontwarn javax.xml.**
#-dontwarn com.google.gson.**
#-keep class com.epapyrus.plugpdf.core.** { *; }
#-keep class com.logdog.** { *; }
#-dontwarn fi.harism.**
# SimpleXML
#-keep class org.simpleframework.** { *; }
#-keepattributes Signature, *Annotation*
# Picasso
#-dontwarn com.squareup.okhttp.**
#-dontwarn org.conscrypt**
#-dontwarn org.codehaus.mojo.**

#-keep class com.firebase.** { *; }
#-keep class org.apache.** { *; }
#-keepnames class com.fasterxml.jackson.** { *; }
#-keepnames class javax.servlet.** { *; }
#-keepnames class org.ietf.jgss.** { *; }
#-dontwarn org.apache.**
#-dontwarn org.w3c.dom.**
#-keepclassmembers class android.support.design.internal.BottomNavigationMenuView {
#    boolean mShiftingMode;
#}
#-assumenosideeffects class android.util.Log {
#    public static boolean isLoggable(java.lang.String, int);
#    public static int d(...);
#    public static int w(...);
#    public static int v(...);
#    public static int i(...);
#}

