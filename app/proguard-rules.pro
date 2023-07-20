# Crashlytics
-keepattributes SourceFile,LineNumberTable

# Adapty
-keep class com.adapty.** { *; }

# Google Drive API
-keep class * extends com.google.api.client.json.GenericJson { *; }
-keep class com.google.api.services.drive.** { *; }
-keepclassmembers class * { @com.google.api.client.util.Key <fields>; }