pluginManagement {
    plugins {
        kotlin("jvm") version "1.5.0"
        id("com.gradle.plugin-publish") version "0.14.0"
    }
}

rootProject.name = "android-version-project"

include(":plugin")

project(":plugin").name = "android-version"