plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.kmp.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.skie) apply false
}

val checkModuleBoundaries = tasks.register("checkModuleBoundaries") {
    group = "verification"
    description = "Runs every module boundary check."
}

gradle.projectsEvaluated {
    checkModuleBoundaries.configure {
        dependsOn(
            subprojects.mapNotNull { project ->
                project.tasks.findByName("checkModuleBoundaries")
            },
        )
    }
}

subprojects {
    // Nested modules are all named "api" or "impl". Gradle's default capability
    // is group:name, so a shared group collapses every impl onto one project.
    group = "com.mvpmodular." + path.removePrefix(":").replace(':', '.')
    version = "0.1.0"

    tasks.withType<Test>().configureEach {
        testLogging {
            events("passed", "failed", "skipped")
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
}
