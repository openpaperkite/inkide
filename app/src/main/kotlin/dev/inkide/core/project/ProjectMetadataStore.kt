package dev.inkide.core.project

import java.nio.file.Files
import java.nio.file.Path
import java.util.Properties

class ProjectMetadataStore {

    fun initialize(
        projectRoot: Path,
    ) {
        val inkDirectory =
            projectRoot.resolve(".ink")

        Files.createDirectories(
            inkDirectory,
        )

        val workspaceFile =
            inkDirectory.resolve(
                "workspace.properties",
            )

        if (!Files.exists(workspaceFile)) {
            val properties =
                Properties().apply {
                    setProperty(
                        "version",
                        "1",
                    )
                }

            Files.newOutputStream(
                workspaceFile,
            ).use { output ->
                properties.store(
                    output,
                    "Ink IDE Project Workspace",
                )
            }
        }
    }
}