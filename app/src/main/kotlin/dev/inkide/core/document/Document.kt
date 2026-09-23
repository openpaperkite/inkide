package dev.inkide.core.document

import kotlinx.coroutines.flow.StateFlow

interface Document {

    val id: DocumentId

    val name: String

    val state: StateFlow<DocumentState>
}