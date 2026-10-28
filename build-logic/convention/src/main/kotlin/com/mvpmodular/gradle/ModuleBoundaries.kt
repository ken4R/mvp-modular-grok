package com.mvpmodular.gradle

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency

fun Project.registerModuleBoundaries() {
    val boundaryCheck = tasks.register("checkModuleBoundaries") {
        group = "verification"
        description = "Fails when this module breaks the api/impl dependency rules."
        doLast { verifyModuleBoundaries() }
    }
    afterEvaluate {
        tasks.findByName("check")?.dependsOn(boundaryCheck)
    }
}

private fun Project.verifyModuleBoundaries() {
    val projectDependencies = declaredProjectPaths()
    val violations = mutableListOf<String>()

    fun forbid(reason: String, predicate: (String) -> Boolean) {
        projectDependencies.filter(predicate).forEach { dependency ->
            violations += "$path -> $dependency ($reason)"
        }
    }

    when {
        path == ":core:common" -> {
            forbid("core:common has no project dependencies") { true }
        }
        path == ":core:network" || path == ":core:storage" -> {
            forbid("network and storage depend only on :core:common") { it != ":core:common" }
        }
        path == ":core:di" -> {
            forbid("core:di stays inside :core") { !it.startsWith(":core:") }
        }
        path.matches(Regex(":feature:[^:]+:api")) -> {
            forbid("a feature api depends only on :core:common and other feature apis") { dependency ->
                dependency != ":core:common" && !dependency.matches(Regex(":feature:[^:]+:api"))
            }
        }
        path.matches(Regex(":feature:[^:]+:impl")) -> {
            val feature = path.split(":")[2]
            forbid("an impl never depends on another impl") { dependency ->
                dependency.matches(Regex(":feature:[^:]+:impl")) && dependency != path
            }
            forbid("an impl does not depend on the apps") { it == ":shared" || it == ":androidApp" }
            if (":feature:$feature:api" !in projectDependencies) {
                violations += "$path is missing api(:feature:$feature:api)"
            }
        }
        path == ":shared" -> {
            forbid("shared does not depend on the Android app") { it == ":androidApp" }
        }
        path == ":androidApp" -> {
            forbid("androidApp depends only on :shared") { it != ":shared" }
        }
    }

    violations += scanForbiddenImports()
    if (violations.isNotEmpty()) {
        throw GradleException(
            "Module boundary violations:\n" + violations.joinToString(separator = "\n"),
        )
    }
}

private fun Project.declaredProjectPaths(): Set<String> =
    configurations.mapNotNull { configuration ->
        runCatching {
            configuration.dependencies.withType(ProjectDependency::class.java).map { it.path }
        }.getOrNull()
    }.flatten().filter { dependency -> dependency != path }.toSet()

private fun Project.scanForbiddenImports(): List<String> {
    val sources = projectDir.resolve("src")
    if (!sources.exists()) {
        return emptyList()
    }
    val implImport = Regex("""import\s+com\.mvpmodular\.feature\.([A-Za-z0-9]+)\.impl\b""")
    val featureImport = Regex("""import\s+com\.mvpmodular\.feature\.""")
    val violations = mutableListOf<String>()
    sources.walkTopDown().filter { file -> file.isFile && file.extension == "kt" }.forEach { file ->
        val relative = file.relativeTo(rootProject.projectDir).invariantSeparatorsPath
        file.readLines().forEach { line ->
            val trimmed = line.trim()
            val importedFeature = implImport.find(trimmed)?.groupValues?.get(1)
            if (importedFeature != null) {
                val allowed = path == ":shared" ||
                    path == ":androidApp" ||
                    path == ":feature:$importedFeature:impl"
                if (!allowed) {
                    violations += "$relative imports :feature:$importedFeature:impl"
                }
            }
            if (path.startsWith(":core:") && featureImport.containsMatchIn(trimmed)) {
                violations += "$relative imports a feature from core"
            }
        }
    }
    return violations
}
