import com.mvpmodular.gradle.isMacHost
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("mvp.kmp.library")
}

if (isMacHost()) {
    apply(plugin = "co.touchlab.skie")
}

kotlin {
    if (isMacHost()) {
        targets.withType<KotlinNativeTarget>().configureEach {
            binaries.framework {
                baseName = "Shared"
                isStatic = true
                export(project(":core:common"))
                export(project(":core:di"))
                export(project(":feature:auth:api"))
                export(project(":feature:auth:impl"))
                export(project(":feature:profile:api"))
                export(project(":feature:profile:impl"))
                export(project(":feature:payments:api"))
                export(project(":feature:payments:impl"))
                export(project(":feature:notifications:api"))
                export(project(":feature:notifications:impl"))
            }
        }
    }
    sourceSets {
        commonMain.dependencies {
            api(project(":core:di"))
            api(project(":feature:auth:impl"))
            api(project(":feature:profile:impl"))
            api(project(":feature:payments:impl"))
            api(project(":feature:notifications:impl"))
        }
    }
}
