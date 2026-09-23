package dev.inkide.core.filesystem

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Files
import java.nio.file.Path

class LocalFileSystem : FileSystem {

    override suspend fun children(
        path: Path,
    ): List<Path> = withContext(Dispatchers.IO) {
        Files.list(path).use { stream ->
            stream
                .sorted(
                    compareBy<Path>(
                        { !Files.isDirectory(it) },
                        {
                            it.fileName
                                .toString()
                                .lowercase()
                        },
                    ),
                )
                .toList()
        }
    }

    override suspend fun readText(
        path: Path,
    ): String = withContext(Dispatchers.IO) {
        Files.readString(path)
    }

    override suspend fun writeText(
        path: Path,
        content: String,
    ) = withContext(Dispatchers.IO) {
        Files.writeString(
            path,
            content,
        )

        Unit
    }

    override suspend fun createFile(
        path: Path,
    ) = withContext(Dispatchers.IO) {
        Files.createFile(path)
        Unit
    }

    override suspend fun createDirectory(
        path: Path,
    ) = withContext(Dispatchers.IO) {
        Files.createDirectory(path)
        Unit
    }

    override fun isDirectory(
        path: Path,
    ): Boolean {
        return Files.isDirectory(path)
    }
}