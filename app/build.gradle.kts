plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.example.doubletapphomework4"
    compileSdk = 34
    buildToolsVersion = "34.0.0"

    defaultConfig {
        applicationId = "com.example.doubletapphomework4"
        minSdk = 24
        targetSdk = 34
        multiDexEnabled = true
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
//    implementation("org.testng:testng:6.9.6")
    kapt("androidx.room:room-compiler:2.6.1")

    // Hilt
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("com.google.dagger:hilt-android:2.49")
    implementation("androidx.hilt:hilt-work:1.2.0")
    kapt("androidx.hilt:hilt-compiler:1.2.0")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    // Tests
    implementation("androidx.arch.core:core-testing:2.2.0")
    implementation(libs.junit)
    implementation("org.mockito:mockito-inline:3.9.0")
    implementation("org.mockito:mockito-core:5.12.0")
    implementation("androidx.compose.ui:ui-test-junit4:1.6.7")
    implementation("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.1")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.49")
    implementation("com.google.dagger:hilt-android-testing:2.49")
    implementation("androidx.compose.ui:ui-test-junit4-android:1.6.7")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.7")

    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}