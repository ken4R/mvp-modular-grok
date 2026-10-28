plugins {
    id("mvp.kmp.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:common"))
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
    }
}

if (System.getProperty("os.name").orEmpty().contains("mac", ignoreCase = true)) {
    kotlin {
        sourceSets.named("iosMain").dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}
