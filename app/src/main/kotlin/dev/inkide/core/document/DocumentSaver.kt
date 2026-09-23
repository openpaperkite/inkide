package dev.inkide.core.document

import dev.inkide.core.filesystem.FileSystem

class DocumentSaver(
    private val fileSystem: FileSystem,
) {

    suspend fun save(
        document: Document,
    ) {
        require(
            document is FileDocument,
        ) {
            "Document is not backed by a file."
        }

        val state =
            document.state.value

        fileSystem.writeText(
            path = document.path,
            content = state.content,
        )

        document.markSaved()
    }
}