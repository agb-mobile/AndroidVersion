plugins {
    id("com.android.application")
    kotlin("android")
    id("si.kamino.version")
}

repositories {
    jcenter()
    google()
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.3")

    defaultConfig {
        targetSdkVersion(30)
        minSdkVersion(21)
    }

    buildTypes {

    }

    buildFeatures {

    }

    splits {
        abi {
            isEnable = true
        }
        density {
            isEnable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.10")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")
}
//
//buildscript {
//    val kotlin_version by extra("1.4.21")
//    dependencies {
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
//    }
//}

androidVersion {
    versionName(si.kamino.gradle.extensions.name.SemanticFileVersion::class.java) {
        file.set(rootProject.file("config/version.properties"))
    }
    versionCode(si.kamino.gradle.extensions.code.FileVersionCode::class.java) {
        file.set(rootProject.file("config/version.properties"))
    }

    variants {
        register("debug") {
            suffix = "-debug"
        }
    }
}

