buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
    }
}
// build.gradle.kts (корневой проект)
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
