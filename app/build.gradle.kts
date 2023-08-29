import java.util.Properties

plugins {
    id(Plugins.ANDROID_APPLICATION)
    id(Plugins.JETBRAINS_KOTLIN_ANDROID)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.GOOGLE_SERVICES)
    id(Plugins.FIREBASE_CRASHLYTICS)
    id(Plugins.DAGGER_HILT_PLUGIN)
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
    load(keystorePropertiesFile.inputStream())
}

tasks.withType<Test> {
    useJUnitPlatform()
}

android {

    signingConfigs {
        create("develop") {
            keyAlias = keystoreProperties["keyAlias_dev"] as String
            keyPassword = keystoreProperties["keyPassword_dev"] as String
            storeFile = file(keystoreProperties["storeFile_dev"] as String)
            storePassword = keystoreProperties["storePassword_dev"] as String
        }

        create("release") {
            keyAlias = keystoreProperties["keyAlias_prod"] as String
            keyPassword = keystoreProperties["keyPassword_prod"] as String
            storeFile = file(keystoreProperties["storeFile_prod"] as String)
            storePassword = keystoreProperties["storePassword_prod"] as String
        }

    }

    namespace = DefaultConfig.NAME_SAPCE
    compileSdk = DefaultConfig.COMPILE_SDK_VERSION

    defaultConfig {
        applicationId = DefaultConfig.APPLICATION_ID
        minSdk = DefaultConfig.MIN_SDK_VERSION
        targetSdk = DefaultConfig.TARGET_SDK_VERSION
        versionCode = DefaultConfig.VERSION_CODE
        versionName = DefaultConfig.VERSION_NAME

        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            properties["kakao.native.app.key"] as String
        )
        resValue(
            "string",
            "KAKAO_NATIVE_APP_KEY_FULL",
            properties["kakao.native.app.key.full"] as String
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    flavorDimensions("1.0")

    productFlavors {
        create("dev") {
            // 개발용
            buildConfigField(
                "String",
                "BASE_URL",
                "\"http://runway-dev-env.eba-h3xrns2m.ap-northeast-2.elasticbeanstalk.com/\""
            )
        }

        create("prod") {
            // 배포용
            buildConfigField("String", "BASE_URL", "\"https://prod.runwayserver.shop/\"")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }

    packagingOptions {
        resources {
            exclude("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(Dependencies.ANDROID_CORE_KTX)
    implementation(Dependencies.ACTIVITY_COMPOSE)
    implementation(Dependencies.COMPOSE_UI)
    implementation(Dependencies.COMPOSE_UI_TOOLING)
    implementation(Dependencies.COMPOSE_UI_PREVIEW)
    implementation(Dependencies.COMPOSE_MATERIAL3)
    implementation(Dependencies.COMPOSE_MATERIAL)
    implementation(Dependencies.COMPOSE_UI_UTIL)
    implementation(Dependencies.APPCOMPAT)
    debugImplementation(Dependencies.COMPOSE_UI_TOOLING)
    debugImplementation(Dependencies.COMPOSE_UI_PREVIEW)
    debugImplementation(Dependencies.COMPOSE_UI_TEST_MANIFEST)

    // Test lib
    testImplementation(Testing.JUNIT_JUPITER)
    testImplementation(Testing.JUNIT_PLATFORM_COMMONS)
    testImplementation(Testing.ASSERTJ_CORE)
    testImplementation(Testing.JUNIT_JUPITER_PARAMS)
    androidTestImplementation(Testing.UI_TEST_JUNIT4)
    androidTestImplementation(Testing.JUNIT)
    androidTestImplementation(Testing.ESPRESSO_CORE)

    // compose navigate
    implementation(Dependencies.NAVIGATION_COMPOSE)
    implementation(Dependencies.NAVIGATION_RUNTIME_KTX)

    // Naver Map
    implementation(Dependencies.NAVER_MAP_COMPOSE)
    implementation(Dependencies.NAVER_SDK) {
        exclude(group = Dependencies.ANDROID_SUPPORT)
    }

    // FusedLocation
    implementation(Dependencies.PLAY_SERVICES_LOCATION)

    // Lifecycles only (without ViewModel or LiveData)
    implementation(Dependencies.LIFECYCLE_RUNTIME_KTX)
    implementation(Dependencies.LIFECYCLE_RUNTIME_COMPOSE)
    implementation(Dependencies.LIFECYCLE_COMMON_JAVA8)

    // Accompanist
    implementation(Dependencies.ACCOMPANIST_SYSTEMUICONTROLLER)
    implementation(Dependencies.ACCOMPANIST_PAGER)
    implementation(Dependencies.ACCOMPANIST_PAGER_INDICATORS)
    implementation(Dependencies.ACCOMPANIST_WEBVIEW)

    // Coil
    implementation(Dependencies.COIL_COMPOSE)
    implementation(Dependencies.COIL_BASE)
    implementation(Dependencies.GLIDE_COMPOSE)
    implementation(Dependencies.GLIDE_OKHTTP3_INTEGRATION)
    kapt(Dependencies.GLIDE_COMPILER)

    // Kakao Login
    implementation(Dependencies.KAKAO_LOGIN)


    // 중첩 Column 스크롤을 위한 커스텀 Toolbar 레이아웃
    // https://blog.onebone.me/post/jetpack-compose-nested-scroll/
    // 후에 직접 구현하는 것으로 수정
    implementation(Dependencies.TOOLBAR_COMPOSE)

    // firebase crashlytics
    implementation(platform(Dependencies.FIREBASE_BOM))
    implementation(Dependencies.FIREBASE_CRASHLYTICS)
    implementation(Dependencies.FIREBASE_ANALYTICS)

    // Lottie
    implementation(Dependencies.LOTTIE)

    // Hilt
    implementation(Dependencies.HILT_ANDROID)
    kapt(Dependencies.HILT_COMPILER)
    implementation(Dependencies.HILT_NAVIGATION_COMPOSE)
    implementation(Dependencies.DAGGER)
    kapt(Dependencies.DAGGER_COMPILER)

    // DataStore
    implementation(Dependencies.DATASTORE)

    // Paging Compose
    implementation(Dependencies.PAGING_RUNTIME)
    implementation(Dependencies.PAGING_COMPOSE)

}