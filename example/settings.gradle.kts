pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenLocal()
    }

    plugins {
        kotlin("jvm") version "1.5.10"
        kotlin("android") version "1.5.10"
        id("com.android.application") version "7.0.0-beta02"
        id("si.kamino.version") version "3.0.1"
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.android.application" -> useModule("com.android.tools.build:gradle:7.0.0-beta02")
                "si.kamino.version" -> useModule("si.kamino.gradle:android-version:3.0.2-SNAPSHOT")
            }
        }
    }
}

rootProject.name = "android-version-example"
