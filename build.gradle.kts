// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { setUrl("https://oss.jfrog.org/artifactory/oss-snapshot-local") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
        classpath("org.jetbrains.kotlin:kotlin-allopen:1.3.50")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.2")
        classpath("com.google.gms:google-services:4.3.4")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.4.1")
        classpath("com.google.gms:oss-licenses:0.9.2")

    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

tasks.register("clean", Delete::class.java) {
    delete(buildDir)
}
