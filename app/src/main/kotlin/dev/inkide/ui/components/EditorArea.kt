package dev.inkide.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.inkide.core.document.Document
import dev.inkide.core.document.DocumentId
import dev.inkide.core.workspace.WorkspaceState
import dev.inkide.ui.IdeDimensions
import dev.inkide.ui.InkColors

@Composable
fun EditorArea(
    state: WorkspaceState,
    onDocumentSelected: (DocumentId) -> Unit,
    onDocumentClosed: (DocumentId) -> Unit,
    onDocumentContentChanged: (
        DocumentId,
        String,
    ) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(InkColors.Graphite),
    ) {
        EditorTabs(
            documents = state.documents,
            activeDocumentId = state.activeDocumentId,
            onDocumentSelected = onDocumentSelected,
            onDocumentClosed = onDocumentClosed,
        )

        EditorContent(
            document = state.activeDocument,
            onContentChanged = { content ->
                val document =
                    state.activeDocument
                        ?: return@EditorContent

                onDocumentContentChanged(
                    document.id,
                    content,
                )
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun EditorTabs(
    documents: List<Document>,
    activeDocumentId: DocumentId?,
    onDocumentSelected: (DocumentId) -> Unit,
    onDocumentClosed: (DocumentId) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IdeDimensions.EditorTabBarHeight)
            .background(InkColors.DarkGraphite),
    ) {
        documents.forEach { document ->

            val documentState by
            document.state.collectAsState()

            val isActive =
                document.id == activeDocumentId

            EditorTab(
                document = document,
                active = isActive,
                modified = documentState.isModified,
                onClick = {
                    onDocumentSelected(
                        document.id,
                    )
                },
                onClose = {
                    onDocumentClosed(
                        document.id,
                    )
                },
            )
        }
    }
}

@Composable
private fun EditorTab(
    document: Document,
    active: Boolean,
    modified: Boolean,
    onClick: () -> Unit,
    onClose: () -> Unit,
) {
    val background =
        if (active) {
            InkColors.Graphite
        } else {
            InkColors.DarkGraphite
        }

    val textColor =
        if (active) {
            InkColors.Green
        } else {
            InkColors.TextMuted
        }

    Row(
        modifier = Modifier
            .height(IdeDimensions.EditorTabBarHeight)
            .background(background)
            .clickable(onClick = onClick)
            .padding(
                start = 16.dp,
                end = 8.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = document.name,
            color = textColor,
            fontSize = 14.sp,
        )

        if (modified) {
            Spacer(
                modifier = Modifier.width(6.dp),
            )

            Text(
                text = "●",
                color = InkColors.Orange,
                fontSize = 10.sp,
            )
        }

        Spacer(
            modifier = Modifier.width(10.dp),
        )

        Text(
            text = "×",
            color = InkColors.TextMuted,
            fontSize = 16.sp,
            modifier = Modifier.clickable {
                onClose()
            },
        )
    }
}

@Composable
private fun EditorContent(
    document: Document?,
    onContentChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (document == null) {
        Box(
            modifier = modifier
                .background(InkColors.Graphite),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "No document open",
                color = InkColors.TextMuted,
                fontSize = 16.sp,
            )
        }

        return
    }

    val documentState by
    document.state.collectAsState()

    val verticalScrollState =
        rememberScrollState()

    val horizontalScrollState =
        rememberScrollState()

    Box(
        modifier = modifier
            .background(InkColors.Graphite),
    ) {
        BasicTextField(
            value = documentState.content,
            onValueChange = onContentChanged,
            singleLine = false,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    verticalScrollState,
                )
                .horizontalScroll(
                    horizontalScrollState,
                )
                .padding(16.dp),
            textStyle = TextStyle(
                color = InkColors.TextPrimary,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
            ),
            cursorBrush = SolidColor(
                InkColors.Green,
            ),
        )

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(
                verticalScrollState,
            ),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
        )

        HorizontalScrollbar(
            adapter = rememberScrollbarAdapter(
                horizontalScrollState,
            ),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(),
        )
    }
}