package dev.inkide.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
        )

        EditorContent(
            document = state.activeDocument,
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
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IdeDimensions.EditorTabBarHeight)
            .background(InkColors.DarkGraphite),
    ) {
        documents.forEach { document ->

            val isActive =
                document.id == activeDocumentId

            EditorTab(
                document = document,
                active = isActive,
                onClick = {
                    onDocumentSelected(
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
    onClick: () -> Unit,
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

    Box(
        modifier = Modifier
            .height(IdeDimensions.EditorTabBarHeight)
            .background(background)
            .clickable(
                onClick = onClick,
            )
            .padding(
                horizontal = 16.dp,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = document.name,
            color = textColor,
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun EditorContent(
    document: Document?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(InkColors.Graphite)
            .padding(24.dp),
    ) {
        if (document == null) {
            Text(
                text = "No document open",
                color = InkColors.TextMuted,
                fontSize = 16.sp,
            )

            return
        }

        Text(
            text = document.content,
            color = InkColors.TextPrimary,
            fontSize = 15.sp,
        )
    }
}