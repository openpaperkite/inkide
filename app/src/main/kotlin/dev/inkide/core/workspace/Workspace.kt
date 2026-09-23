package dev.inkide.core.workspace

import dev.inkide.core.document.Document
import dev.inkide.core.document.DocumentId
import kotlinx.coroutines.flow.StateFlow

interface Workspace {

    val state: StateFlow<WorkspaceState>

    fun openDocument(
        document: Document,
    )

    fun closeDocument(
        id: DocumentId,
    )

    fun activateDocument(
        id: DocumentId,
    )

    fun closeAllDocuments()

    fun updateDocumentContent(
        id: DocumentId,
        content: String,
    )
}