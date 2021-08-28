plugins {
    id("io.gitlab.arturbosch.detekt").version(LIBRARY_VERSION_DETEKT)
}

buildscript {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${LIBRARY_VERSION_GRADLE_TOOLS}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${LIBRARY_VERSION_KOTLIN}")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${LIBRARY_VERSION_HILT}")
    }
}

allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }

    apply(from = "$rootDir/gradle/scripts/detekt.gradle")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
    delete("$rootDir/detekt/reports")
}
