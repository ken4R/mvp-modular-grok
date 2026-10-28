import com.mvpmodular.gradle.libsCatalog
import com.mvpmodular.gradle.versionInt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("dev.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("mvp.module-boundaries")
}

val catalog = libsCatalog()

android {
    namespace = "com.mvpmodular.android"
    compileSdk = catalog.versionInt("compileSdk")
    defaultConfig {
        applicationId = "com.mvpmodular.android"
        minSdk = catalog.versionInt("minSdk")
        targetSdk = catalog.versionInt("targetSdk")
        versionCode = 1
        versionName = "0.1.0"
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE*"
            excludes += "/META-INF/NOTICE*"
        }
    }
    lint {
        abortOnError = true
        checkReleaseBuilds = true
    }
}

detekt {
    buildUponDefaultConfig = true
    parallel = true
    config.setFrom(rootProject.file("config/detekt/detekt.yml"))
}

ktlint {
    android.set(true)
    ignoreFailures.set(false)
    filter {
        exclude("**/build/**")
        exclude("**/generated/**")
    }
}
