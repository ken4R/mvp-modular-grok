import com.mvpmodular.gradle.library
import com.mvpmodular.gradle.libsCatalog

plugins {
    id("mvp.kmp.library")
}

val featureApiPath = path.removeSuffix(":impl") + ":api"
val catalog = libsCatalog()

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(featureApiPath))
            api(catalog.library("koin-core"))
            api(catalog.library("lifecycle-viewmodel"))
            implementation(project(":core:common"))
            implementation(catalog.library("koin-core-viewmodel"))
        }
    }
}
