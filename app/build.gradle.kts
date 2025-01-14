plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
//    23
    id("kotlin-kapt")
}

android {
    namespace = "hal.tutorials.a7minutesworkout"
    compileSdk = 33

    defaultConfig {
        applicationId = "hal.tutorials.a7minutesworkout"
        minSdk = 24
        targetSdk = 33
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
//   2. tambahkan ini:
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
//    def room_version = '2.3.0'
//    def activityVersion = '1.3.1'

//    ROOM AND LIFECYCLE DEPEDENCIES
    implementation("androidx.room:room-runtime:2.3.0")
    implementation("androidx.room:room-common:2.3.0")
    implementation("androidx.room:room-ktx:2.3.0")
    kapt ("androidx.room:room-compiler:2.3.0")
//    kotlin extensions for coroutine support with room
    implementation("androidx.room:room-ktx:2.3.0")
//    kotlin extension for coroutine support with activites
    implementation ("androidx.activity:activity-ktx:1.3.1")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}