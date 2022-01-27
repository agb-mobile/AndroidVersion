pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenLocal()
    }

    plugins {
        kotlin("jvm") version "1.6.10"
        kotlin("android") version "1.6.10"
        id("com.android.application") version "7.1.0"
        id("si.kamino.version") version "3.2.0"
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.android.application" -> useModule("com.android.tools.build:gradle:7.1.0")
                "si.kamino.version" -> useModule("si.kamino.gradle:android-version:3.2.0")
            }
        }
    }
}

rootProject.name = "android-version-example"
