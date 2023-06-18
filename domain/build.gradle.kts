plugins {
    id(Plugins.ANDROID_LIBRARY)
    id(Plugins.JETBRAINS_KOTLIN_ANDROID)
    id(Plugins.KOTLIN_KAPT)
}

android {
    namespace = DefaultConfig.DOMAIN_NAME_SAPCE
    compileSdk = DefaultConfig.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = DefaultConfig.MIN_SDK_VERSION
        targetSdk = DefaultConfig.TARGET_SDK_VERSION

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
    implementation(Dependencies.DATASTORE)

    // Gson
    implementation(Dependencies.GSON)
    implementation(Dependencies.RETROFIT_CONVERTER_MOSHI)

    // Paging Compose
    implementation(Dependencies.PAGING_RUNTIME)
    implementation(Dependencies.PAGING_COMPOSE)

    // Room DB
    implementation(Dependencies.ROOM_RUNTIME)
    kapt(Dependencies.ROOM_COMPILER)
    // 코루틴 활용을 위해
    implementation(Dependencies.ROOM_KTX)

    // Naver Map
    implementation(Dependencies.NAVER_MAP_COMPOSE)
    implementation(Dependencies.NAVER_SDK) {
        exclude(group = Dependencies.ANDROID_SUPPORT)
    }
}