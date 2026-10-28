import com.mvpmodular.gradle.isMacHost
import com.mvpmodular.gradle.library
import com.mvpmodular.gradle.libsCatalog
import com.mvpmodular.gradle.mvpNamespace
import com.mvpmodular.gradle.versionInt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("dev.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("mvp.module-boundaries")
}

val catalog = libsCatalog()

base {
    archivesName.set(path.removePrefix(":").replace(':', '-'))
}

kotlin {
    jvmToolchain(21)
    android {
        namespace = mvpNamespace()
        compileSdk = catalog.versionInt("compileSdk")
        minSdk = catalog.versionInt("minSdk")
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            // Nested modules are all named "api" or "impl". A shared module name
            // makes their .kotlin_module files collide and the compiler drops packages.
            moduleName.set(path.removePrefix(":").replace(':', '-'))
        }
        withHostTest { }
    }
    if (isMacHost()) {
        iosArm64()
        iosSimulatorArm64()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(catalog.library("coroutines-core"))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(catalog.library("coroutines-test"))
        }
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

