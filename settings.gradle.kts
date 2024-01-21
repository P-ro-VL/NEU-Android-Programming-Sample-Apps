pluginManagement {
    repositories {
        google()
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

rootProject.name = "SampleCode"
include(":app")
include(":app:clock")
include(":app:unitconverter")
include(":app:calculator")
include(":app:expensify")
include(":app:test")
