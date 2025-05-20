// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("androidx.navigation.safeargs.kotlin") version libs.versions.safeArgs apply false
    kotlin("plugin.serialization") version "2.0.21"
    id("com.google.devtools.ksp") version libs.versions.ksp apply false
    id ("com.google.dagger.hilt.android") version libs.versions.hiltVersion apply false
}