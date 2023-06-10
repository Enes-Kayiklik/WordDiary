buildscript {
    val hilt_version by extra("2.44")

    dependencies {
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hilt_version")
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.3.1" apply false
    id("com.android.library") version "7.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.mikepenz.aboutlibraries.plugin") version "10.5.2" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.0" apply false
}