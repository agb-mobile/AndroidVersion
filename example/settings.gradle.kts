pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenLocal()
    }

    plugins {
        kotlin("jvm") version "1.4.21"
        kotlin("android") version "1.4.21"
        id("com.android.application") version "4.1.1"
        id("si.kamino.version") version "2.0.0-SNAPSHOT"
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.android.application" -> useModule("com.android.tools.build:gradle:4.1.1")
            }
        }
    }
}

rootProject.name = "android-version-example"
