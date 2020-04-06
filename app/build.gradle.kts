import com.danhdueexoictif.buildsrc.BuildVersions
import com.danhdueexoictif.buildsrc.Deps
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("jacoco")
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")
    id("com.google.gms.oss.licenses.plugin")
}

apply {
    from("../buildSrc/autodimension.gradle")
    from("../buildSrc/jacoco.gradle")
//    from("../checkci.gradle")
}

jacoco {
    toolVersion = "0.8.1+"
}

repositories {
    maven { setUrl("https://jitpack.io") }
    jcenter()
}

android {
    compileSdkVersion(BuildVersions.compileSdkVersion)
    buildToolsVersion(BuildVersions.buildToolsVersion)

    androidExtensions {
        isExperimental = true
    }

    bundle {
        density.enableSplit = true
        abi.enableSplit = true
        language.enableSplit = false
    }

    lintOptions {
        isCheckReleaseBuilds = false
        isAbortOnError = false
        disable("MissingTranslation")
    }

    defaultConfig {
        applicationId = "com.danhdueexoictif.androidgenericadapter"
        minSdkVersion(BuildVersions.minSdk)
        targetSdkVersion(BuildVersions.targetSdk)
        versionCode = BuildVersions.versionCode
        versionName = BuildVersions.versionName
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
        create("d3t3Debug") {
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storeFile = file("../debug.keystore")
            storePassword = "android"
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs["d3t3Debug"]
            isDebuggable = true
            applicationIdSuffix = ".debug"
            isTestCoverageEnabled = false
            resValue("string", "app_name", "Android Generic Adapter(Sta)")
            buildConfigField(
                    "String",
                    "SHARED_PREFERENCES_NAME",
                    "\"com.danhdueexoictif.androidgenericadapter.debug\""
            )
            buildConfigField(
                    "String",
                    "BASE_URL",
                    "\"https://www.danhdue.com/api/V1/\""
            )
            buildConfigField(
                    "String",
                    "BASE_AUTH_USER_NAME",
                    "\"danhdue\""
            )
            buildConfigField(
                    "String",
                    "BASE_AUTH_PASSWORD",
                    "\"d3t3\""
            )
        }

        getByName("release") {
            signingConfig = signingConfigs["d3t3Debug"]
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            isTestCoverageEnabled = false
            isUseProguard = true
            resValue("string", "app_name", "Android Generic Adapter")
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
            buildConfigField(
                    "String",
                    "SHARED_PREFERENCES_NAME",
                    "\"com.danhdueexoictif.androidgenericadapter\""
            )
            buildConfigField(
                    "String",
                    "BASE_URL",
                    "\"https://www.danhdue.com/api/V1/\""
            )
            buildConfigField(
                    "String",
                    "BASE_AUTH_USER_NAME",
                    "\"danhdue\""
            )
            buildConfigField(
                    "String",
                    "BASE_AUTH_PASSWORD",
                    "\"d3t3\""
            )
        }

        applicationVariants.all {
            buildConfigField(
                    "String",
                    "SITE_SERIAL",
                    "\"HDN9DanhDueExOICTIFvSAt3HqfFz5DJDZT3yzRzl592FWFQd9l954N76W7Z81ZNIwm\""
            )
        }
    }

    compileOptions {
        setTargetCompatibility(1.8)
        setSourceCompatibility(1.8)
    }

    kotlinOptions {
        // We have to add the explicit cast before accessing the options itself.
        // If we don't, it does not work: "unresolved reference: jvmTarget"
        val options = this as KotlinJvmOptions
        options.jvmTarget = "1.8"
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/LICENSE-notice.md")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/*.kotlin_module")
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    dataBinding {
        isEnabled = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Support.appCompat)
    implementation(Deps.Support.coreKtx)
    implementation(Deps.Support.constraintLayout)
    implementation(Deps.Support.material)
    implementation(Deps.Support.recyclerView)
    implementation(Deps.Support.cardView)
    implementation(Deps.Support.v4)
    implementation(Deps.Support.recyclerView)
    implementation(Deps.multiDex)
    implementation(project(":YoutubePlayer"))

    // Crashlytics
    implementation(Deps.crashlytics) { isTransitive = true }
    implementation(Deps.firebaseCrashlytics) { isTransitive = true }

    // Timber
    implementation(Deps.timber)

    // koin
    implementation(Deps.Koin.core)
    implementation(Deps.Koin.scope)
    implementation(Deps.Koin.viewModel)

    //navigation
    implementation(Deps.Navigation.uiKtx)
    implementation(Deps.Navigation.fragmentKtx)
    implementation(Deps.Navigation.runtimeKtx)

    // Retrofit
    implementation(Deps.Retrofit.runtime)
    implementation(Deps.Retrofit.adapter)
    implementation(Deps.Retrofit.gson)
    implementation(Deps.Retrofit.mock)
    implementation(Deps.Retrofit.okhttpLoggingInterceptor)
    implementation(Deps.Retrofit.scalarsConverter)
    implementation(Deps.Retrofit.okhttp3)

    // toro
    implementation(Deps.Toro.core)
    implementation(Deps.Toro.ext)
    implementation(Deps.Google.exoPlayer)
    api(Deps.Google.exoPlayerUI)

    // joda time
    implementation(Deps.jodaTime)

    // Gson
    implementation(Deps.gson)

    // Glide
    implementation(Deps.Glide.runtime)
    kapt(Deps.Glide.compiler)

    // ReactiveX
    implementation(Deps.ReactiveX.rxJava2)
    implementation(Deps.ReactiveX.rxAndroid)
    implementation(Deps.ReactiveX.rxKotlin)
    api(Deps.ReactiveX.rxJava2)

    // Coroutines
    implementation(Deps.Kotlin.coroutines)
    implementation(Deps.Kotlin.kotlintest)
    implementation(Deps.Retrofit.mockwebserver)
    implementation(Deps.Google.guava)

    // Lifecycle
    implementation(Deps.Lifecycle.viewModelKtx)
    implementation(Deps.Lifecycle.liveDataKtx)
    implementation(Deps.Lifecycle.savedState)
    implementation(Deps.Lifecycle.extensions)
    kapt(Deps.Lifecycle.compiler)

    // Room
    implementation(Deps.Room.runtime)
    annotationProcessor(Deps.Room.compiler) // For Kotlin use kapt instead of annotationProcessor
    implementation(Deps.Room.ktx) // optional - Kotlin Extensions and Coroutines support for Room
    implementation(Deps.Room.rxJava2) // optional - RxJava support for Room
    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation(Deps.Room.guava)
    testImplementation(Deps.Room.testing) // Test helpers

    // firebase
    implementation(Deps.firebaseCore)
    implementation(Deps.firebase)
    implementation(Deps.Google.auth)
    implementation(Deps.fcm)

    // pager indicator
    implementation(Deps.pagerIndicator)
    implementation(Deps.circleindicator)

    // Testing
    testImplementation(Deps.Test.junit)
    androidTestImplementation(Deps.Test.testExt)
    androidTestImplementation(Deps.Test.espresso)
    implementation(Deps.Test.mockito)
    testImplementation(Deps.Test.mockk)
    testImplementation(Deps.Test.coreTesting)
    androidTestImplementation(Deps.Test.androidMockk)
    androidTestImplementation(Deps.Test.koinTest)
    testImplementation(Deps.Test.koinTest)
    androidTestImplementation(Deps.Test.coreTesting)
    androidTestImplementation(Deps.Test.core)
    androidTestImplementation(Deps.Test.runner)
    androidTestImplementation(Deps.Test.rules)
    implementation(Deps.Test.slf4j)
    implementation(Deps.Google.licenses)
}

tasks.register("fullCoverageReport", JacocoReport::class) {
    //    dependsOn("createDebugCoverageReport")
    dependsOn("testDebugUnitTest")

    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }

    val fileFilters = listOf(
            "**/R.class",
            "**/R$*.class",
            "**/BR.class",
            "**/DataBinder*.class",
            "**/*DataBinding*.*",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*Test*.*",
            "**/android/**/*.*",
            "**/*Fragment.*",
            "**/*Activity.*",
            "**/androidx/**/*.*",
            "**/com.facebook/**/*.*",
            "**/fabric/**/*.*",
            "**/CrashReportingTree.class",
            "**/*Companion.class",
            "**/org.koin/**/*.*",
            "**/databinding/*.*",
            "**/com/danhdueexoictif/androidgenericadapter/widgets/**/*.*",
            "**/com/danhdueexoictif/androidgenericadapter/generated/**/*.*",
            "**/com/danhdueexoictif/androidgenericadapter/data/bean/**/*.*",
            "**/com/danhdueexoictif/androidgenericadapter/data/remote/request/**/*.*",
            "**/com/danhdueexoictif/androidgenericadapter/data/remote/response/**/*.*",
            "**/com/danhdueexoictif/androidgenericadapter/di/**/*.*",
            "**/com/danhdueexoictif/androidgenericadapter/ui/base/**/*.*"
    )

    val debugTree = fileTree(
            mapOf(
                    "dir" to "$buildDir/intermediates/classes/debug",
                    "excludes" to fileFilters
            )
    )

    val mainSrc = "${project.projectDir}/src/main/java"

    sourceDirectories.setFrom(files(listOf(mainSrc)))
    classDirectories.setFrom(files(listOf(debugTree)))

    executionData.setFrom(
            fileTree(
                    mapOf(
                            "dir" to "$buildDir",
                            "includes" to listOf(
                            "jacoco/testDebugUnitTest.exec",
                            "outputs/code-coverage/connected/*coverage.ec"
                    )
                    )
            )
    )
}


project.tasks.whenTaskAdded {
    when(name) {
        "testReleaseUnitTest" -> enabled = false
        "testMockDebugUnitTest" -> enabled = false
        "testMockReleaseUnitTest" -> enabled = false
        "testPrdDebugUnitTest" -> enabled = false
        "testPrdReleaseUnitTest" -> enabled = false
        "connectedAndroidTest" -> enabled = false
        "connectedDebugAndroidTest" -> enabled = false
        "connectedMockDebugAndroidTest" -> enabled = false
        "connectedPrdDebugAndroidTest" -> enabled = false
    }
}

configurations.all {
    resolutionStrategy.force(Deps.Support.appCompat)
}
