plugins {
    id("mvp.kmp.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:common"))
            implementation(libs.koin.core)
        }
    }
}
