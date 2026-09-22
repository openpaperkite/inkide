package dev.inkide.core.workspace

import dev.inkide.core.document.Document
import dev.inkide.core.document.DocumentId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DefaultWorkspace : Workspace {

    private val _state =
        MutableStateFlow(
            WorkspaceState(),
        )

    override val state: StateFlow<WorkspaceState> =
        _state.asStateFlow()

    override fun openDocument(
        document: Document,
    ) {
        val currentState = _state.value

        val existingDocument =
            currentState.documents.firstOrNull {
                it.id == document.id
            }

        if (existingDocument != null) {
            _state.value = currentState.copy(
                activeDocumentId = existingDocument.id,
            )

            return
        }

        _state.value = currentState.copy(
            documents = currentState.documents + document,
            activeDocumentId = document.id,
        )
    }

    override fun closeDocument(
        id: DocumentId,
    ) {
        val currentState = _state.value

        val remainingDocuments =
            currentState.documents.filterNot {
                it.id == id
            }

        if (
            remainingDocuments.size ==
            currentState.documents.size
        ) {
            return
        }

        val nextActiveDocumentId =
            if (currentState.activeDocumentId == id) {
                remainingDocuments.lastOrNull()?.id
            } else {
                currentState.activeDocumentId
            }

        _state.value = currentState.copy(
            documents = remainingDocuments,
            activeDocumentId = nextActiveDocumentId,
        )
    }

    override fun activateDocument(
        id: DocumentId,
    ) {
        val currentState = _state.value

        val documentExists =
            currentState.documents.any {
                it.id == id
            }

        if (!documentExists) {
            return
        }

        _state.value = currentState.copy(
            activeDocumentId = id,
        )
    }

    override fun closeAllDocuments() {
        _state.value = WorkspaceState()
    }
}