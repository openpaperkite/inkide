package dev.inkide.core.document

class TextDocument(
    override val id: DocumentId,
    override val name: String,
    initialContent: String = "",
) : Document {

    override var content: String = initialContent
        private set

    override var isModified: Boolean = false
        private set

    fun updateContent(
        newContent: String,
    ) {
        if (content == newContent) {
            return
        }

        content = newContent
        isModified = true
    }

    fun markSaved() {
        isModified = false
    }
}