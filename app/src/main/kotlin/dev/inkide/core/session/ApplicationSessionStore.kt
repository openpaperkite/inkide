package dev.inkide.core.session

import dev.inkide.core.config.AppDirectories
import java.nio.file.Files
import java.nio.file.Path
import java.util.Properties

class ApplicationSessionStore {

    private val sessionFile: Path
        get() =
            AppDirectories
                .configDirectory
                .resolve("session.properties")

    fun saveLastProject(
        path: Path,
    ) {
        Files.createDirectories(
            sessionFile.parent,
        )

        val properties =
            Properties().apply {
                setProperty(
                    "lastProject",
                    path
                        .toAbsolutePath()
                        .normalize()
                        .toString(),
                )
            }

        Files.newOutputStream(
            sessionFile,
        ).use { output ->
            properties.store(
                output,
                "Ink IDE Session",
            )
        }
    }

    fun loadLastProject(): Path? {
        if (!Files.exists(sessionFile)) {
            return null
        }

        val properties =
            Properties()

        Files.newInputStream(
            sessionFile,
        ).use { input ->
            properties.load(input)
        }

        val value =
            properties.getProperty(
                "lastProject",
            ) ?: return null

        return Path.of(value)
    }
}