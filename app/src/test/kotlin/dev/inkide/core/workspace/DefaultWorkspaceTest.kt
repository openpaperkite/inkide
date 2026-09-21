package dev.inkide.core.workspace

import dev.inkide.core.document.DocumentId
import dev.inkide.core.document.TextDocument
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultWorkspaceTest {

    @Test
    fun `opening a document makes it active`() {
        val workspace = DefaultWorkspace()

        val document = TextDocument(
            id = DocumentId("main"),
            name = "Main.kt",
        )

        workspace.openDocument(document)

        assertEquals(
            document,
            workspace.activeDocument,
        )
    }

    @Test
    fun `opening the same document twice does not duplicate it`() {
        val workspace = DefaultWorkspace()

        val document = TextDocument(
            id = DocumentId("main"),
            name = "Main.kt",
        )

        workspace.openDocument(document)
        workspace.openDocument(document)

        assertEquals(
            1,
            workspace.documents.size,
        )
    }

    @Test
    fun `closing active document activates remaining document`() {
        val workspace = DefaultWorkspace()

        val first = TextDocument(
            id = DocumentId("first"),
            name = "First.kt",
        )

        val second = TextDocument(
            id = DocumentId("second"),
            name = "Second.kt",
        )

        workspace.openDocument(first)
        workspace.openDocument(second)

        workspace.closeDocument(
            second.id,
        )

        assertEquals(
            first,
            workspace.activeDocument,
        )
    }

    @Test
    fun `closing final document leaves no active document`() {
        val workspace = DefaultWorkspace()

        val document = TextDocument(
            id = DocumentId("main"),
            name = "Main.kt",
        )

        workspace.openDocument(document)

        workspace.closeDocument(
            document.id,
        )

        assertNull(
            workspace.activeDocument,
        )
    }
}