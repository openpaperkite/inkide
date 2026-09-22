package dev.inkide.core.filesystem

import java.nio.file.Path

interface FileSystem {

    suspend fun children(
        path: Path,
    ): List<Path>

    fun isDirectory(
        path: Path,
    ): Boolean
}