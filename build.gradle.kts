// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
   /* configurations.classpath {
        resolutionStrategy {
            // Hilt Gradle plugin đôi khi bị kéo về javapoet cũ (thiếu canonicalName()) do conflict với plugin khác
            force("com.squareup:javapoet:1.13.0")
        }
    }
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
        classpath("com.squareup:javapoet:1.13.0")
    }*/
}


plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.google.services) apply false
}