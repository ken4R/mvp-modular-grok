plugins {
    id("mvp.kmp.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:common"))
            api(project(":core:network"))
            api(project(":core:storage"))
            implementation(libs.koin.core)
        }
    }
}
