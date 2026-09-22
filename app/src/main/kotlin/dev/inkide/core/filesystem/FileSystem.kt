package dev.inkide.core.filesystem

import java.nio.file.Path

interface FileSystem {

    suspend fun children(
        path: Path,
    ): List<Path>

    suspend fun readText(
        path: Path,
    ): String

    suspend fun createFile(
        path: Path,
    )

    suspend fun createDirectory(
        path: Path,
    )

    fun isDirectory(
        path: Path,
    ): Boolean
}