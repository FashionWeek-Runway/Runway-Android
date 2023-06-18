import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("dagger.hilt.android.plugin")
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

    namespace = "com.cmc12th.runway"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.cmc12th.runway"
        minSdk = 21
        targetSdk = 33
        versionCode = 7
        versionName = "1.15"

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
            buildConfigField("String", "BASE_URL", "\"https://dev.runwayserver.shop/\"")
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

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.compose.ui:ui:1.3.3")
    implementation("androidx.compose.ui:ui-tooling:1.3.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.3.3")
    implementation("androidx.compose.material3:material3:1.1.0-alpha05")
    implementation("androidx.compose.material:material:1.3.1")
    implementation("androidx.navigation:navigation-runtime-ktx:2.5.3")
    implementation("androidx.compose.ui:ui-util:1.3.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.3.3")
    debugImplementation("androidx.compose.ui:ui-tooling-preview:1.3.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.3.3")

    // Test lib
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.platform:junit-platform-commons:1.9.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.3.3")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // compose navigate
    implementation("androidx.navigation:navigation-compose:2.5.3")

    // Coil
    implementation("io.coil-kt:coil-compose:2.2.2")

    // Naver Map
    implementation("io.github.fornewid:naver-map-compose:1.2.3")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.naver.maps:map-sdk:3.16.1") {
        exclude(group = "com.android.support")
    }
    // MarkerClusting
    implementation("io.github.ParkSangGwon:tedclustering-naver:1.0.2")

    // FusedLocation
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Lifecycles only (without ViewModel or LiveData)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.1")

    // Accompanist - Statusbar Color바꾸기
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.28.0")
    // Accompanist - Pager
    implementation("com.google.accompanist:accompanist-pager:0.29.1-alpha")
    // Accompanist - Webview
    implementation("com.google.accompanist:accompanist-webview:0.29.1-alpha")

    // Coil
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("io.coil-kt:coil-base:2.2.2")
    implementation("com.github.bumptech.glide:compose:1.0.0-alpha.1")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.11.0")
    kapt("android.arch.lifecycle:compiler:1.1.1")
    kapt("com.github.bumptech.glide:compiler:4.14.2")
//    implementation(deps.coil.compose)
//    implementation(deps.coil.base)
//
//    // Glide
//    implementation(deps.glide.compose)
//    implementation(deps.glide.okhttp3_integration)
//    kapt(deps.glide.compiler)

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // Kakao Login
    implementation("com.kakao.sdk:v2-user:2.12.1")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:31.2.3"))


    // 중첩 Column 스크롤을 위한 커스텀 Toolbar 레이아웃
    // https://blog.onebone.me/post/jetpack-compose-nested-scroll/
    // 후에 직접 구현하는 것으로 수정
    implementation("me.onebone:toolbar-compose:2.3.5")

    // firebase crashlytics
    implementation(platform("com.google.firebase:firebase-bom:31.2.3"))
    implementation("com.google.firebase:firebase-crashlytics-ktx:18.3.5")
    implementation("com.google.firebase:firebase-analytics-ktx:21.2.0")

    // Lottie
    implementation("com.airbnb.android:lottie-compose:6.0.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.43.2")
    kapt("com.google.dagger:hilt-compiler:2.43.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("com.google.dagger:dagger:2.43.2")
    kapt("com.google.dagger:dagger-compiler:2.43.2")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Paging Compose
    implementation("androidx.paging:paging-runtime:3.1.1")
    implementation("androidx.paging:paging-compose:1.0.0-alpha18")

}