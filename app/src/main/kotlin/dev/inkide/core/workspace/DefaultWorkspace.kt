package dev.inkide.core.workspace

import dev.inkide.core.document.Document
import dev.inkide.core.document.DocumentId

class DefaultWorkspace : Workspace {

    private val openDocuments =
        mutableListOf<Document>()

    override val documents: List<Document>
        get() = openDocuments.toList()

    override var activeDocument: Document? = null
        private set

    override fun openDocument(
        document: Document,
    ) {
        val existingDocument =
            openDocuments.firstOrNull {
                it.id == document.id
            }

        if (existingDocument != null) {
            activeDocument = existingDocument
            return
        }

        openDocuments.add(document)

        activeDocument = document
    }

    override fun closeDocument(
        id: DocumentId,
    ) {
        val document =
            openDocuments.firstOrNull {
                it.id == id
            } ?: return

        val wasActive =
            activeDocument?.id == id

        openDocuments.remove(document)

        if (wasActive) {
            activeDocument =
                openDocuments.lastOrNull()
        }
    }

    override fun activateDocument(
        id: DocumentId,
    ) {
        activeDocument =
            openDocuments.firstOrNull {
                it.id == id
            }
    }
}