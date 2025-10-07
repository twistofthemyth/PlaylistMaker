plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.practicum.playlistmaker"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.practicum.playlistmaker"
        minSdk = 29
        targetSdk = 36
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

    buildFeatures {
        viewBinding = true
        compose = true
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    val retrofitVersion = "2.11.0"
    val navVersion = "2.9.3"
    val roomVersion = "2.7.2"

    val composeBom = platform("androidx.compose:compose-bom:2025.09.01")
    implementation(composeBom)

    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.core", "core-ktx", "1.16.0")
    implementation("androidx.appcompat", "appcompat", "1.7.1")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:1.11.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
    implementation("androidx.activity", "activity", "1.10.1")
    implementation("androidx.constraintlayout", "constraintlayout", "2.2.1")
    implementation("androidx.navigation", "navigation-fragment-ktx", navVersion)
    implementation("androidx.navigation", "navigation-ui-ktx", navVersion)
    implementation("androidx.room", "room-runtime", roomVersion)
    implementation("androidx.room", "room-ktx", roomVersion)
    implementation("com.google.android.material", "material", "1.12.0")
    implementation("com.google.code.gson", "gson", "2.13.1")
    implementation("com.squareup.retrofit2", "retrofit", retrofitVersion)
    implementation("com.squareup.retrofit2", "converter-gson", retrofitVersion)
    implementation("com.github.bumptech.glide", "glide", "4.16.0")
    implementation("io.insert-koin", "koin-android", "3.3.0")
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-android", "1.10.2")

    testImplementation("junit", "junit", "4.13.2")

    androidTestImplementation("androidx.test.ext", "junit", "1.3.0")
    androidTestImplementation("androidx.test.espresso", "espresso-core", "3.7.0")
    androidTestImplementation(composeBom)

    debugImplementation("androidx.compose.ui:ui-tooling")

    kapt("androidx.room", "room-compiler", roomVersion)
}