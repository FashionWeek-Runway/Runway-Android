buildscript {
    apply(from = "versions.gradle")

//    ext {
//        compose_version = "1.3.3"
//        nav_version = "2.5.3"
//    }

    dependencies {
        classpath("com.google.gms:google-services:4.3.15")
    }
}
// 모든 하위 프로젝트/모듈에 공통적인 구성 옵션을 추가하는 최상위 빌드
plugins {
    id("com.android.application") version "7.3.1" apply false
    id("com.android.library") version "7.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.google.dagger.hilt.android") version "2.43.2" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.firebase.crashlytics") version "2.9.4" apply false
}