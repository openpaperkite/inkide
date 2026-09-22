package dev.inkide.core.config

import java.nio.file.Files

class AppDirectoriesInitializer {

    fun initialize() {
        Files.createDirectories(
            AppDirectories.configDirectory,
        )

        Files.createDirectories(
            AppDirectories.projectsDirectory,
        )
    }
}