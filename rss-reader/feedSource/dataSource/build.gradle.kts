plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdk = COMPILE_SDK_VERSION
    buildToolsVersion = BUILD_TOOLS_VERSION

    defaultConfig {
        minSdk = MIN_SDK_VERSION
        targetSdk = TARGET_SDK_VERSION

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
}

dependencies {
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${LIBRARY_VERSION_KOTLIN_COROUTINES}")

    implementation(platform("com.squareup.okhttp3:okhttp-bom:${LIBRARY_VERSION_OKHTTP}"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    api(project(":feedSource:resources"))
}
