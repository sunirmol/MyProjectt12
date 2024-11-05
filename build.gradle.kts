plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}


buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        //noinspection GradlePluginVersion
        classpath ("com.android.tools.build:gradle:<latest_version>")
        classpath ("com.google.gms:google-services:<latest_version>")
        classpath ("com.android.tools.build:gradle:8.1.1")
        classpath ("com.google.gms:google-services:4.3.15")

    }
}