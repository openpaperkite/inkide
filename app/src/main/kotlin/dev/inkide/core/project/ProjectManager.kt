package dev.inkide.core.project

import dev.inkide.core.session.ApplicationSessionStore
import dev.inkide.core.workspace.Workspace
import java.nio.file.Files
import java.nio.file.Path

class ProjectManager(
    private val projectService: ProjectService,
    private val workspace: Workspace,
    private val sessionStore: ApplicationSessionStore,
    private val metadataStore: ProjectMetadataStore,
) {

    suspend fun openProject(
        path: Path,
    ) {
        val normalizedPath =
            path
                .toAbsolutePath()
                .normalize()

        require(
            Files.isDirectory(normalizedPath),
        ) {
            "Project path is not a directory: $normalizedPath"
        }

        workspace.closeAllDocuments()

        metadataStore.initialize(
            normalizedPath,
        )

        projectService.openProject(
            normalizedPath,
        )

        sessionStore.saveLastProject(
            normalizedPath,
        )
    }

    suspend fun createProject(
        parentDirectory: Path,
        projectName: String,
    ): Path {
        require(
            projectName.isNotBlank(),
        ) {
            "Project name cannot be blank."
        }

        val projectDirectory =
            parentDirectory
                .resolve(projectName.trim())
                .toAbsolutePath()
                .normalize()

        require(
            !Files.exists(projectDirectory),
        ) {
            "A file or directory already exists at $projectDirectory"
        }

        Files.createDirectories(
            projectDirectory,
        )

        openProject(
            projectDirectory,
        )

        return projectDirectory
    }

    suspend fun restoreLastProject(): Boolean {
        val lastProject =
            sessionStore.loadLastProject()
                ?: return false

        if (!Files.isDirectory(lastProject)) {
            return false
        }

        openProject(lastProject)

        return true
    }
}