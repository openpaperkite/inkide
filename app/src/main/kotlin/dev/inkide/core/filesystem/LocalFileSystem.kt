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

    override fun isDirectory(
        path: Path,
    ): Boolean {
        return Files.isDirectory(path)
    }
}