package dev.inkide.core.project

import dev.inkide.core.filesystem.FileSystem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.nio.file.Path

class ProjectService(
    private val fileSystem: FileSystem,
) {

    private val _state =
        MutableStateFlow(
            ProjectState(),
        )

    val state: StateFlow<ProjectState> =
        _state.asStateFlow()

    suspend fun openProject(
        rootPath: Path,
    ) {
        _state.value = ProjectState(
            rootPath = rootPath,
            loading = true,
        )

        try {
            val root =
                buildNode(
                    path = rootPath,
                )

            _state.value = ProjectState(
                rootPath = rootPath,
                rootNode = root,
            )
        } catch (exception: Exception) {
            _state.value = ProjectState(
                rootPath = rootPath,
                error = exception.message
                    ?: "Unable to open project.",
            )
        }
    }

    private suspend fun buildNode(
        path: Path,
    ): ProjectNode {
        val directory =
            fileSystem.isDirectory(path)

        if (!directory) {
            return ProjectNode(
                name = path.fileName.toString(),
                path = path,
                isDirectory = false,
            )
        }

        val children =
            fileSystem
                .children(path)
                .filterNot(::shouldIgnore)
                .map { child ->
                    buildNode(child)
                }

        return ProjectNode(
            name = path.fileName?.toString()
                ?: path.toString(),
            path = path,
            isDirectory = true,
            children = children,
        )
    }

    private fun shouldIgnore(
        path: Path,
    ): Boolean {
        return path.fileName
            ?.toString() in ignoredNames
    }

    companion object {

        private val ignoredNames =
            setOf(
                ".ink",
                ".git",
                ".gradle",
                ".idea",
                ".kotlin",
                "build",
            )
    }
}