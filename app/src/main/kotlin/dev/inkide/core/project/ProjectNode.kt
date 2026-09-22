package dev.inkide.core.project

import java.nio.file.Path

data class ProjectNode(
    val name: String,
    val path: Path,
    val isDirectory: Boolean,
    val children: List<ProjectNode> = emptyList(),
)