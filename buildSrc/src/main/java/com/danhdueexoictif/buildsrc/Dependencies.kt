package com.danhdueexoictif.buildsrc

object Version {
    const val kotlin = "1.3.70"
    const val coroutines = "1.3.9"
    const val kotlintest = "3.4.2"
    const val room = "2.2.1"
    const val timber = "4.7.1"
    const val koin = "2.1.5"
    const val appCompat = "1.2.0-alpha01"
    const val material = "1.2.1"
    const val constraintLayout = "1.1.3"
    const val coreKtx = "1.1.0"
    const val testExt = "1.1.1"
    const val junit = "4.12"
    const val espresso = "3.2.0"
    const val firebaseCrashlytics = "17.0.0-beta03"
    const val navigation = "2.2.0-rc01"
    const val retrofit = "2.7.2"
    const val okhttp = "4.4.0"
    const val retrofitScalars = "2.7.2"
    const val mockwebserver = "4.4.0"
    const val gson = "2.8.6"
    const val lifecycle = "2.2.0-rc01"
    const val savedState = "1.0.0-rc01"
    const val glide = "4.11.0"
    const val rxJava2 = "2.2.13"
    const val rxJava3 = "3.0.0-RC4"
    const val rxAndroid = "2.1.1"
    const val rxKotlin = "2.4.0"
    const val testCore = "1.2.0"
    const val testRunner = "1.2.0"
    const val testRules = "1.2.0"
    const val androidxCoreTesting = "2.1.0"
    const val koinTesting = "2.0.0-beta-1"
    const val androidMockk = "1.9"
    const val mockk = "1.9.3"
    const val mockito = "2.27.0"
    const val mockitoKotlin = "2.2.0"
    const val slf4j = "1.7.30"
    const val robolectric = "4.3"
    const val multiDex = "2.0.1"
    const val toro = "3.7.0.2010003"
    const val firebase = "17.2.2"
    const val fcm = "20.1.0"
    const val auth = "17.0.0"
    const val pagerIndicator = "1.0.3"
    const val exoplayer2 = "2.10.3"
    const val guava = "28.2-jre"
    const val circleindicator = "2.1.4"
    const val androidxAnnotations = "1.0.2"
    const val cardView = "1.0.0"
    const val legacySupportV4 = "1.0.0"
    const val recyclerView = "1.1.0"
    const val jodaTime = "2.10.5"
    const val crashlytics = "2.10.1@aar"
    const val firebaseCore = "17.2.2"
    const val ossLicenses = "17.0.0"
    const val lottie = "3.4.0"
}

object BuildVersions {
    const val compileSdkVersion = 33
    const val buildToolsVersion = "33.0.1"
    const val minSdk = 21
    const val targetSdk = 33
    const val versionCode = 1
    const val versionName = "1.0.0"
}

object Deps {

    const val timber = "com.jakewharton.timber:timber:${Version.timber}"
    const val firebaseCore = "com.google.firebase:firebase-core:${Version.firebaseCore}"
    const val crashlytics = "com.crashlytics.sdk.android:crashlytics:${Version.crashlytics}"
    const val firebaseCrashlytics =
        "com.google.firebase:firebase-crashlytics:${Version.firebaseCrashlytics}"
    const val multiDex = "androidx.multidex:multidex:${Version.multiDex}"
    const val firebase = "com.google.firebase:firebase-analytics:${Version.firebase}"
    const val fcm = "com.google.firebase:firebase-messaging:${Version.fcm}"
    const val androidxAnnotations = "androidx.annotation:annotation:${Version.androidxAnnotations}"
    const val jodaTime = "joda-time:joda-time:${Version.jodaTime}"

    object FlowBinding {
        private const val flowBindingVersion: String = "0.12.0"
        const val android: String =
            "io.github.reactivecircus.flowbinding:flowbinding-android:$flowBindingVersion"
        const val swipeRefresh: String =
            "io.github.reactivecircus.flowbinding:flowbinding-swiperefreshlayout:$flowBindingVersion"
        const val lifecycle: String =
            "io.github.reactivecircus.flowbinding:flowbinding-lifecycle:$flowBindingVersion"
    }

    object Kotlin {
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Version.kotlin}"
        const val test = "org.jetbrains.kotlin:kotlin-test-junit:${Version.kotlin}"
        const val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlin}"
        const val allopen = "org.jetbrains.kotlin:kotlin-allopen:${Version.kotlin}"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutines}"
        const val androidCoroutine =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutines}"
        const val kotlintest = "io.kotlintest:kotlintest-runner-junit5:${Version.kotlintest}"
        const val kotlinxCoroutineTest =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.coroutines}"
    }

    object Support {
        const val appCompat = "androidx.appcompat:appcompat:${Version.appCompat}"
        const val v4 = "androidx.legacy:legacy-support-v4:${Version.legacySupportV4}"
        const val recyclerView = "androidx.recyclerview:recyclerview:${Version.recyclerView}"
        const val cardView = "androidx.cardview:cardview:${Version.cardView}"
        const val material = "com.google.android.material:material:${Version.material}"
        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout:${Version.constraintLayout}"
        const val coreKtx = "androidx.core:core-ktx:${Version.coreKtx}"
    }

    object Room {
        const val runtime = "androidx.room:room-runtime:${Version.room}"
        const val compiler = "androidx.room:room-compiler:${Version.room}"
        const val ktx = "androidx.room:room-ktx:${Version.room}"
        const val rxJava2 = "androidx.room:room-rxjava2:${Version.room}"
        const val guava = "androidx.room:room-guava:${Version.room}"
        const val testing = "androidx.room:room-testing:${Version.room}"
    }

    object Koin {
        const val core = "org.koin:koin-android:${Version.koin}"
        const val scope = "org.koin:koin-androidx-scope:${Version.koin}"
        const val viewModel = "org.koin:koin-androidx-viewmodel:${Version.koin}"
    }

    object Test {
        const val junit = "junit:junit:${Version.junit}"
        const val testExt = "androidx.test.ext:junit:${Version.testExt}"
        const val espresso = "androidx.test.espresso:espresso-core:${Version.espresso}"
        const val core = "androidx.test:core:${Version.testCore}"
        const val runner = "androidx.test:runner:${Version.testRunner}"
        const val rules = "androidx.test:rules:${Version.testRules}"
        const val coreTesting = "androidx.arch.core:core-testing:${Version.androidxCoreTesting}"
        const val koinTest = "org.koin:koin-test:${Version.koinTesting}"
        const val androidMockk = "io.mockk:mockk-android:${Version.androidMockk}"
        const val mockk = "io.mockk:mockk:${Version.mockk}"
        const val mockito = "org.mockito:mockito-core:${Version.mockito}"
        const val mockitoInline = "org.mockito:mockito-inline:${Version.mockito}"
        const val mockitoKotlin =
            "com.nhaarman.mockitokotlin2:mockito-kotlin:${Version.mockitoKotlin}"
        const val slf4j = "org.slf4j:slf4j-simple:${Version.slf4j}"
        const val robolectric = "org.robolectric:robolectric:${Version.robolectric}"
    }

    object Navigation {
        const val safeArgsPlugin =
            "androidx.navigation:navigation-safe-args-gradle-plugin:${Version.navigation}"
        const val runtimeKtx = "androidx.navigation:navigation-runtime-ktx:${Version.navigation}"
        const val fragment = "androidx.navigation:navigation-fragment:${Version.navigation}"
        const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Version.navigation}"
        const val ui = "androidx.navigation:navigation-ui:${Version.navigation}"
        const val uiKtx = "androidx.navigation:navigation-ui-ktx:${Version.navigation}"
    }

    object Retrofit {
        const val runtime = "com.squareup.retrofit2:retrofit:${Version.retrofit}"
        const val gson = "com.squareup.retrofit2:converter-gson:${Version.retrofit}"
        const val mock = "com.squareup.retrofit2:retrofit-mock:${Version.retrofit}"
        const val mockwebserver = "com.squareup.okhttp3:mockwebserver:${Version.mockwebserver}"
        const val adapter = "com.squareup.retrofit2:adapter-rxjava2:${Version.retrofit}"
        const val okhttp3 = "com.squareup.okhttp3:okhttp:${Version.okhttp}"
        const val okhttpLoggingInterceptor =
            "com.squareup.okhttp3:logging-interceptor:${Version.okhttp}"
        const val scalarsConverter =
            "com.squareup.retrofit2:converter-scalars:${Version.retrofitScalars}"
    }

    const val gson = "com.google.code.gson:gson:${Version.gson}"

    object Glide {
        const val runtime = "com.github.bumptech.glide:glide:${Version.glide}"
        const val compiler = "com.github.bumptech.glide:compiler:${Version.glide}"
    }

    object ReactiveX {
        const val rxJava2 = "io.reactivex.rxjava2:rxjava:${Version.rxJava2}"
        const val rxJava3 = "io.reactivex.rxjava3:rxjava:${Version.rxJava3}"
        const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Version.rxAndroid}"
        const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Version.rxKotlin}"
    }

    object Lifecycle {
        const val runtime = "androidx.lifecycle:lifecycle-runtime:${Version.lifecycle}"
        const val extensions = "androidx.lifecycle:lifecycle-extensions:${Version.lifecycle}"
        const val java8 = "androidx.lifecycle:lifecycle-common-java8:${Version.lifecycle}"
        const val savedState =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Version.savedState}"
        const val compiler = "androidx.lifecycle:lifecycle-compiler:${Version.lifecycle}"
        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycle}"
        const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.lifecycle}"
    }

    object Toro {
        const val core = "im.ene.toro3:toro:${Version.toro}"
        const val ext = "im.ene.toro3:toro-ext-exoplayer:${Version.toro}"
    }

    object Google {
        const val auth = "com.google.android.gms:play-services-auth:${Version.auth}"
        const val exoPlayer = "com.google.android.exoplayer:exoplayer:${Version.exoplayer2}"
        const val exoPlayerUI = "com.google.android.exoplayer:exoplayer-ui:${Version.exoplayer2}"
        const val guava = "com.google.guava:guava:${Version.guava}"
        const val licenses =
            "com.google.android.gms:play-services-oss-licenses:${Version.ossLicenses}"
    }

    const val pagerIndicator = "com.romandanylyk:pageindicatorview:${Version.pagerIndicator}"
    const val circleindicator = "me.relex:circleindicator:${Version.circleindicator}"
    const val lottie = "com.airbnb.android:lottie:${Version.lottie}"
}