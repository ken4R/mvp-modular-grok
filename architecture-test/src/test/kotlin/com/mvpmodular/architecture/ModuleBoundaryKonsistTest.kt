package com.mvpmodular.architecture

import com.lemonappdev.konsist.api.Konsist
import kotlin.test.Test
import kotlin.test.assertTrue

class ModuleBoundaryKonsistTest {
    @Test
    fun featureImplFilesDoNotImportAnotherFeatureImpl() {
        val scope = Konsist.scopeFromDirectory("feature")
        val violations = mutableListOf<String>()
        val implImport = Regex("com\\.mvpmodular\\.feature\\.([A-Za-z0-9]+)\\.impl")
        scope.files.forEach { file ->
            val normalized = file.path.replace('\\', '/')
            val owner = Regex("/feature/([^/]+)/impl/").find(normalized)?.groupValues?.get(1) ?: return@forEach
            file.imports.forEach { import ->
                val feature = implImport.find(import.name)?.groupValues?.get(1)
                if (feature != null && feature != owner) {
                    violations += file.path + " imports " + import.name
                }
            }
        }
        assertTrue(violations.isEmpty(), violations.joinToString(separator = "\n"))
    }

    @Test
    fun coreDoesNotImportFeatures() {
        val violations = Konsist
            .scopeFromDirectory("core")
            .imports
            .filter { import -> import.name.startsWith("com.mvpmodular.feature.") }
            .map { import -> import.name }
        assertTrue(violations.isEmpty(), violations.joinToString())
    }
}
