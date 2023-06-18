buildscript {
    dependencies {
        classpath(Dependencies.GOOGLE_SERVICE)
    }
}

// 모든 하위 프로젝트/모듈에 공통적인 구성 옵션을 추가하는 최상위 빌드
plugins {
    id(Plugins.ANDROID_APPLICATION) version Versions.AGP apply false
    id(Plugins.ANDROID_LIBRARY) version Versions.AGP apply false
    id(Plugins.JETBRAINS_KOTLIN_ANDROID) version Versions.KOTLIN apply false
    id(Plugins.DAGGER_HILT_ANDROID) version Versions.HILT apply false
    id(Plugins.GOOGLE_SERVICES) version Versions.GOOGLE_SERVICES apply false
    id(Plugins.FIREBASE_CRASHLYTICS) version Versions.FIREBASE_CRASHLYTICS apply false
}