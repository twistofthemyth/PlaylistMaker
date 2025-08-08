plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
}

android {
    namespace = "com.practicum.playlistmaker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.practicum.playlistmaker"
        minSdk = 29
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

    buildFeatures {
        viewBinding = true
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
    val navVersion = "2.9.0"
    val roomVersion = "2.6.1"

    implementation("androidx.core", "core-ktx", "1.15.0")
    implementation("androidx.appcompat", "appcompat", "1.6.1")
    implementation("com.google.android.material", "material", "1.10.0")
    implementation("androidx.activity", "activity", "1.9.3")
    implementation("androidx.constraintlayout", "constraintlayout", "2.1.4")
    implementation("com.google.code.gson", "gson", "2.12.1")
    implementation("com.squareup.retrofit2", "retrofit", retrofitVersion)
    implementation("com.squareup.retrofit2", "converter-gson", retrofitVersion)
    implementation("com.github.bumptech.glide", "glide", "4.14.2")
    implementation("io.insert-koin", "koin-android", "3.3.0")
    implementation("androidx.navigation", "navigation-fragment-ktx", navVersion)
    implementation("androidx.navigation", "navigation-ui-ktx", navVersion)
    implementation("androidx.room", "room-runtime", roomVersion)
    implementation("androidx.room", "room-ktx", roomVersion)
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-android", "1.6.4")

    testImplementation("junit", "junit", "4.13.2")

    androidTestImplementation("androidx.test.ext", "junit", "1.2.1")
    androidTestImplementation("androidx.test.espresso", "espresso-core", "3.6.1")

    kapt("androidx.room", "room-compiler", roomVersion)
}