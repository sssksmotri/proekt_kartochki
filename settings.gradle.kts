// settings.gradle.kts
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version "8.2.2"
        id("org.jetbrains.kotlin.android") version "1.8.10"
        id("com.google.gms.google-services") version "4.3.15"
    }
}

rootProject.name = "Kartochki"
include(":app")
