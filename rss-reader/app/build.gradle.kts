plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"

    defaultConfig {
        minSdk = 26
        targetSdk = 30

        applicationId = "com.github.jonforshort.rssreader"
        versionCode = 1
        versionName = "1.0"
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

    buildFeatures {
        dataBinding = true
    }

    packagingOptions {
        resources {
            excludes.add("META-INF/NOTICE.md")
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/*.kotlin_module")
        }
    }
}

dependencies {

    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${LIBRARY_VERSION_KOTLIN}")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
    implementation("androidx.core:core-ktx:${LIBRARY_VERSION_CORE_KTX}")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.1")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${LIBRARY_VERSION_LIVEDATA}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${LIBRARY_VERSION_LIVEDATA}")
    implementation("androidx.navigation:navigation-fragment-ktx:${LIBRARY_VERSION_NAVIGATION}")
    implementation("androidx.navigation:navigation-ui-ktx:${LIBRARY_VERSION_NAVIGATION}")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.browser:browser:1.3.0")

    implementation("androidx.room:room-runtime:${LIBRARY_VERSION_ROOM}")
    implementation("androidx.room:room-ktx:${LIBRARY_VERSION_ROOM}")
    kapt("androidx.room:room-compiler:${LIBRARY_VERSION_ROOM}")

    implementation("com.github.bumptech.glide:glide:4.12.0")
    kapt("com.github.bumptech.glide:compiler:4.12.0")

    implementation("com.google.dagger:hilt-android:${LIBRARY_VERSION_HILT}")
    kapt("com.google.dagger:hilt-android-compiler:${LIBRARY_VERSION_HILT}")

    implementation(project(":feedContent:fetcher"))
    implementation(project(":feedContent:repo"))
    implementation(project(":feedSource:repo"))

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
