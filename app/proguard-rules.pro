# Proguard rules for CareLoop
# Keep Room entities and DAOs
-keep class com.careloop.app.data.local.** { *; }
-keep class net.sqlcipher.** { *; }

