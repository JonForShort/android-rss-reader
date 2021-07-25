plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}


android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    defaultConfig {
        minSdkVersion(26)
        targetSdkVersion(30)

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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")

    implementation("androidx.room:room-runtime:${LIBRARY_VERSION_ROOM}")
    implementation("androidx.room:room-ktx:${LIBRARY_VERSION_ROOM}")
    kapt("androidx.room:room-compiler:${LIBRARY_VERSION_ROOM}")

    implementation(project(":feedContent:fetcher"))

    testImplementation("androidx.room:room-testing:${LIBRARY_VERSION_ROOM}")
}