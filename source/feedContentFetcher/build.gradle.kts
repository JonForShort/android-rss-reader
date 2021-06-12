plugins {
    id("java-library")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation("com.squareup.retrofit2:retrofit:${LIBRARY_VERSION_RETROFIT}")
    implementation("com.squareup.retrofit2:converter-simplexml:${LIBRARY_VERSION_RETROFIT}")
    implementation("org.simpleframework:simple-xml:${LIBRARY_VERSION_SIMPLE_XML}")
}
