package com.mvpmodular.gradle

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider

fun isMacHost(): Boolean = System.getProperty("os.name").orEmpty().contains("mac", ignoreCase = true)

fun Project.mvpNamespace(): String = "com.mvpmodular." + path.removePrefix(":").replace(':', '.')

fun Project.libsCatalog(): VersionCatalog =
    extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

fun VersionCatalog.library(alias: String): Provider<MinimalExternalModuleDependency> =
    findLibrary(alias).orElseThrow { IllegalStateException("Missing library '$alias' in the version catalog") }

fun VersionCatalog.versionInt(alias: String): Int =
    findVersion(alias)
        .orElseThrow { IllegalStateException("Missing version '$alias' in the version catalog") }
        .requiredVersion
        .toInt()
