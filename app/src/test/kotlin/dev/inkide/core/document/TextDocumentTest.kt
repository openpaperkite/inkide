package dev.inkide.core.document

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TextDocumentTest {

    @Test
    fun `new document is not modified`() {
        val document = TextDocument(
            id = DocumentId("main"),
            name = "Main.kt",
            initialContent = "hello",
        )

        assertFalse(
            document.isModified,
        )
    }

    @Test
    fun `changing content marks document as modified`() {
        val document = TextDocument(
            id = DocumentId("main"),
            name = "Main.kt",
        )

        document.updateContent(
            "fun main() {}",
        )

        assertTrue(
            document.isModified,
        )
    }

    @Test
    fun `marking document as saved resets modified state`() {
        val document = TextDocument(
            id = DocumentId("main"),
            name = "Main.kt",
        )

        document.updateContent(
            "fun main() {}",
        )

        document.markSaved()

        assertFalse(
            document.isModified,
        )
    }

    @Test
    fun `updating with identical content does not mark document modified`() {
        val document = TextDocument(
            id = DocumentId("main"),
            name = "Main.kt",
            initialContent = "hello",
        )

        document.updateContent(
            "hello",
        )

        assertFalse(
            document.isModified,
        )

        assertEquals(
            "hello",
            document.content,
        )
    }
}