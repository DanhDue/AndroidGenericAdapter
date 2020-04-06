import com.danhdueexoictif.buildsrc.BuildVersions
import com.danhdueexoictif.buildsrc.Deps

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(BuildVersions.compileSdkVersion)
    buildToolsVersion(BuildVersions.buildToolsVersion)

    defaultConfig {
        minSdkVersion(BuildVersions.minSdk)
        targetSdkVersion(BuildVersions.targetSdk)
        versionCode = BuildVersions.versionCode
        versionName = BuildVersions.versionName
    }

    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    dataBinding {
        isEnabled = true
    }

    sourceSets {
        getByName("main").res.srcDirs(
            "src/main/res",
            "src/main/res-public"
        )
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Support.appCompat)
    implementation(Deps.Support.coreKtx)
    implementation(Deps.Support.constraintLayout)
    implementation(Deps.Support.material)
    implementation(Deps.Support.v4)
    implementation(Deps.Support.recyclerView)
    testImplementation(Deps.Test.junit)
    androidTestImplementation(Deps.Test.testExt)
    androidTestImplementation(Deps.Test.espresso)
    api(Deps.androidxAnnotations)
    api(Deps.Lifecycle.runtime)

}
