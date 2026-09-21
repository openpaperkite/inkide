package dev.inkide.core.document

interface Document {
    val id: DocumentId
    val name: String
    val content: String
    val isModified: Boolean
}