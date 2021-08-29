plugins {
    id("java-library")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${LIBRARY_VERSION_KOTLIN_COROUTINES}")

    implementation(platform("com.squareup.okhttp3:okhttp-bom:${LIBRARY_VERSION_OKHTTP}"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.12.3")
    implementation("javax.xml.stream:stax-api:1.0-2")

    testImplementation("junit:junit:4.13.2")
}