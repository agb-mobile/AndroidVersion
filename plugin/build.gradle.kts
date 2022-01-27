plugins {
    kotlin("jvm")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish")
    id("maven-publish")
}

repositories {
    jcenter()
    google()
}

dependencies {
    compileOnly("com.android.tools.build:gradle:7.1.0")

    implementation(gradleApi())
    implementation(localGroovy())

    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

}

val projectGroup: String by project
val projectVersion: String by project

group = projectGroup
version = projectVersion

gradlePlugin {
    val version by plugins.creating {
        id = projectGroup
        implementationClass = "si.kamino.gradle.VersionPlugin"
        displayName = "Android Version plugin"
    }
}

val POM_URL: String by project
val POM_SCM_URL: String by project
val POM_DESCRIPTION: String by project
pluginBundle {
    website = POM_URL
    vcsUrl = POM_SCM_URL
    description = POM_DESCRIPTION
    tags = listOf("android", "version", "variants", "splits")

    plugins {
        create("versionPlugin") {
            id = "si.kamino.version"
            displayName = "Android Version plugin"
        }
    }
}
