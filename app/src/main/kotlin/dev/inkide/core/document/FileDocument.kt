package dev.inkide.core.document

import java.nio.file.Path

class FileDocument(
    id: DocumentId,
    name: String,
    val path: Path,
    initialContent: String = "",
) : TextDocument(
    id = id,
    name = name,
    initialContent = initialContent,
)