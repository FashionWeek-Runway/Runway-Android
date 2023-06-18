plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.cmc12th.runway.domain"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        named("release") {
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

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    // Paging Compose
    implementation("androidx.paging:paging-runtime:3.1.1")
    implementation("androidx.paging:paging-compose:1.0.0-alpha18")

    // Room DB
    implementation("androidx.room:room-runtime:2.5.0")
    kapt("androidx.room:room-compiler:2.5.0")

    // 코루틴 활용을 위해
    implementation("androidx.room:room-ktx:2.5.0")

    // MarkerClusting
    implementation("io.github.ParkSangGwon:tedclustering-naver:1.0.2")

    // Naver Map
    implementation("io.github.fornewid:naver-map-compose:1.2.3")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.naver.maps:map-sdk:3.16.1") {
        exclude(group = "com.android.support")
    }
}