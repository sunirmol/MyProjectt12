plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.myprojectt"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myprojectt"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Core libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-auth:22.1.0")

    // Google Play services
    implementation("com.google.android.gms:play-services-auth:20.4.1")
    implementation("com.google.android.gms:play-services-auth-api-phone:18.0.1")
    implementation("com.google.firebase:firebase-storage")
    implementation ("com.firebaseui:firebase-ui-storage:8.0.0")
    implementation ("com.google.firebase:firebase-storage:21.0.1") // Add Firebase Storage dependency
    implementation ("com.google.firebase:firebase-auth:22.0.0")

        implementation ("com.github.bumptech.glide:glide:4.12.0")
        annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

        implementation ("com.itextpdf:itext7-core:7.1.15")

    implementation ("com.google.android.material:material:1.9.0")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.drawerlayout:drawerlayout:1.1.1")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("com.google.firebase:firebase-firestore:24.0.1")
    implementation ("com.google.firebase:firebase-storage:20.0.1")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.google.android.gms:play-services-tasks:17.2.1")


    // Other dependencies
    implementation ("com.karumi:dexter:6.2.3")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation ("com.google.firebase:firebase-storage:20.2.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation(libs.firebase.storage)
    // implementation ("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
