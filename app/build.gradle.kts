plugins {
    kotlin("jvm") version "2.4.20"

    id("org.jetbrains.kotlin.plugin.compose") version "2.4.20"

    id("org.jetbrains.compose") version "1.12.0"
}

group = "dev.inkide"
version = "0.0.1"

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(compose.desktop.currentOs)

    implementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0"
    )
}

compose.desktop {
    application {
        mainClass = "dev.inkide.MainKt"
    }
}