pluginManagement {
    plugins {
        kotlin("jvm") version "2.2.0"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "nnCore"
include("nncore-api")