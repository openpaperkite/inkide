package dev.inkide.core.workspace

import dev.inkide.core.document.DocumentId
import dev.inkide.core.document.TextDocument
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertNotNull

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
            workspace.state.value.activeDocument,
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
            workspace.state.value.documents.size,
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
            workspace.state.value.activeDocument,
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
            workspace.state.value.activeDocument,
        )
    }

    @Test
    fun `activating document changes active document`() {
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

        workspace.activateDocument(
            first.id,
        )

        assertEquals(
            first,
            workspace.state.value.activeDocument,
        )
    }

    @Test
    fun `updating document content changes document`() {
        val workspace = DefaultWorkspace()

        val document = TextDocument(
            id = DocumentId("main"),
            name = "Main.kt",
            initialContent = "old",
        )

        workspace.openDocument(
            document,
        )

        workspace.updateDocumentContent(
            id = document.id,
            content = "new",
        )

        val activeDocument =
            assertNotNull(
                workspace.state.value.activeDocument,
            )

        assertEquals(
            "new",
            activeDocument.state.value.content,
        )
    }

    @Test
    fun `updating document content marks document modified`() {
        val workspace = DefaultWorkspace()

        val document = TextDocument(
            id = DocumentId("main"),
            name = "Main.kt",
        )

        workspace.openDocument(
            document,
        )

        workspace.updateDocumentContent(
            id = document.id,
            content = "changed",
        )

        val activeDocument =
            assertNotNull(
                workspace.state.value.activeDocument,
            )

        assertEquals(
            true,
            activeDocument.state.value.isModified,
        )
    }
}