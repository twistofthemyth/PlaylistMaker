// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version ("8.11.1") apply false
    id("org.jetbrains.kotlin.android") version ("2.2.0") apply false
    id("androidx.navigation.safeargs.kotlin") version ("2.9.3") apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.9.3")
    }
}