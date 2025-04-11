buildscript {
    dependencies {

        classpath( "com.android.tools.build:gradle:8.1.2")
        classpath ( "com.google.dagger:hilt-android-gradle-plugin:2.48.1")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
        classpath("com.google.gms:google-services:4.4.2")

        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.2") 



    }

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }



}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {

    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}