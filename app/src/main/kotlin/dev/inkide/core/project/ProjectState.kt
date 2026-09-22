package dev.inkide.core.project

import java.nio.file.Path

data class ProjectState(
    val rootPath: Path? = null,
    val rootNode: ProjectNode? = null,
    val loading: Boolean = false,
    val error: String? = null,
)