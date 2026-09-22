package dev.inkide.core.document

import dev.inkide.core.filesystem.FileSystem
import java.nio.file.Path

class FileDocumentLoader(
    private val fileSystem: FileSystem,
) {

    suspend fun load(
        path: Path,
    ): TextDocument {
        val content =
            fileSystem.readText(path)

        return TextDocument(
            id = DocumentId(
                path
                    .toAbsolutePath()
                    .normalize()
                    .toUri()
                    .toString(),
            ),
            name = path.fileName.toString(),
            initialContent = content,
        )
    }
}