package dev.inkide.core.workspace

import dev.inkide.core.document.Document
import dev.inkide.core.document.DocumentId

data class WorkspaceState(
    val documents: List<Document> = emptyList(),
    val activeDocumentId: DocumentId? = null,
) {
    val activeDocument: Document?
        get() = documents.firstOrNull {
            it.id == activeDocumentId
        }
}