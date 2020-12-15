@file:Suppress("UnstableApiUsage")

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
    }
}

plugins {
    jacoco
}

apply<JacocoPlugin>()
apply<com.android.build.gradle.api.AndroidBasePlugin>()

project.extensions.getByType(JacocoPluginExtension::class.java).apply {
    toolVersion = "0.8.6"
    // Custom reports directory can be specfied like this:
    // reportsDir = file("$projectDir/build/reports/jacoco")
}

tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
    }
}

project.afterEvaluate {

    extensions.findByType(com.android.build.gradle.internal.dsl.BaseAppModuleExtension::class.java)
        ?.run {
            applicationVariants
                .toList()
                .forEach { variant ->
                    val variantName = variant.name
                    val testTaskName = "test${variantName.capitalize()}UnitTest"
                    registerReport(
                        project = project,
                        variantName = variantName,
                        testTaskName = testTaskName
                    )
                }
        }
    extensions.findByType(com.android.build.gradle.LibraryExtension::class.java)
        ?.run {
            libraryVariants
                .toList()
                .forEach { variant ->
                    val variantName = variant.name
                    val testTaskName = "test${variantName.capitalize()}UnitTest"
                    registerReport(
                        project = project,
                        variantName = variantName,
                        testTaskName = testTaskName
                    )
                }
        }
}

fun registerReport(project: Project, variantName: String, testTaskName: String) {
    project.tasks.create("${testTaskName}Coverage", JacocoReport::class.java) {
        dependsOn(testTaskName)

        group = "Coverage"
        description = "Generate Jacoco coverage reports for the ${variantName.capitalize()} build."

        reports {
            html.isEnabled = true
            xml.isEnabled = true
            xml.destination = file("${project.rootDir}/.ci-reports/coverage/coverage.xml")
            html.destination = file("${project.rootDir}/.ci-reports/coverage/")
        }

        val excludes = listOf(
            "**/R.class",
            "**/R$*.class"
        )

        val javaClasses = fileTree(
            mapOf(
                "dir" to "$buildDir/intermediates/classes/debug",
                "excludes" to excludes
            )
        )

        val kotlinClasses = fileTree(
            mapOf(
                "dir" to "${buildDir}/tmp/kotlin-classes/${variantName}",
                "excludes" to excludes
            )
        )

        classDirectories.setFrom(files(listOf(javaClasses, kotlinClasses)))

        sourceDirectories.setFrom(
            files(
                listOf(
                    "$project.projectDir/src/main/java",
                    "$project.projectDir/src/${variantName}/java",
                    "$project.projectDir/src/main/kotlin",
                    "$project.projectDir/src/${variantName}/kotlin"
                )
            )
        )

        executionData.setFrom(files("${project.buildDir}/jacoco/${testTaskName}.exec"))
    }
}
