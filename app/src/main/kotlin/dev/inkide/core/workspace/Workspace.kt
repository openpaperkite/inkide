package dev.inkide.core.workspace

import dev.inkide.core.document.Document
import dev.inkide.core.document.DocumentId

interface Workspace {

    val documents: List<Document>

    val activeDocument: Document?

    fun openDocument(
        document: Document,
    )

    fun closeDocument(
        id: DocumentId,
    )

    fun activateDocument(
        id: DocumentId,
    )
}