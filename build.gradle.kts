plugins {
    id("java")
    kotlin("jvm") version "2.2.0"
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "ru.nnedition.nncore"
    version = "1.0"

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
    }

    kotlin {
        jvmToolchain(21)
    }
}