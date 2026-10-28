pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "mvp-modular"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":androidApp")
include(":shared")
include(":architecture-test")

include(":core:common")
include(":core:network")
include(":core:storage")
include(":core:di")

include(":feature:auth:api")
include(":feature:auth:impl")
include(":feature:profile:api")
include(":feature:profile:impl")
include(":feature:payments:api")
include(":feature:payments:impl")
include(":feature:notifications:api")
include(":feature:notifications:impl")
