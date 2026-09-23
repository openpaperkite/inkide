package dev.inkide.core.document

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class TextDocument(
    override val id: DocumentId,
    override val name: String,
    initialContent: String = "",
) : Document {

    private val _state =
        MutableStateFlow(
            DocumentState(
                content = initialContent,
                isModified = false,
            ),
        )

    override val state: StateFlow<DocumentState> =
        _state.asStateFlow()

    fun updateContent(
        newContent: String,
    ) {
        val currentState = _state.value

        if (currentState.content == newContent) {
            return
        }

        _state.value = currentState.copy(
            content = newContent,
            isModified = true,
        )
    }

    fun markSaved() {
        val currentState = _state.value

        if (!currentState.isModified) {
            return
        }

        _state.value = currentState.copy(
            isModified = false,
        )
    }
}