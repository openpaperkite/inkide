package dev.inkide.core.document

import dev.inkide.core.filesystem.FileSystem
import java.nio.file.Path

class FileDocumentLoader(
    private val fileSystem: FileSystem,
) {

    suspend fun load(
        path: Path,
    ): FileDocument {
        val content =
            fileSystem.readText(path)

        return FileDocument(
            id = DocumentId(
                path
                    .toAbsolutePath()
                    .normalize()
                    .toUri()
                    .toString(),
            ),
            name = path.fileName.toString(),
            path = path
                .toAbsolutePath()
                .normalize(),
            initialContent = content,
        )
    }
}