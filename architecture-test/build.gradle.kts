plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
    id("mvp.module-boundaries")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    testImplementation(kotlin("test-junit"))
    testImplementation(libs.konsist)
}

tasks.withType<Test>().configureEach {
    useJUnit()
    systemProperty("repo.root", rootDir.absolutePath)
}

detekt {
    buildUponDefaultConfig = true
    parallel = true
    config.setFrom(rootProject.file("config/detekt/detekt.yml"))
}

ktlint {
    ignoreFailures.set(false)
    filter {
        exclude("**/build/**")
    }
}
