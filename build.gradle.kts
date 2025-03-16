plugins {
    kotlin("jvm") version "2.1.10"
}

group = "arc.kafka"
version = "1.0-SNAPSHOT"

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.apache.kafka:kafka-clients:3.9.0")
    }

    kotlin {
        jvmToolchain(21)
    }
}