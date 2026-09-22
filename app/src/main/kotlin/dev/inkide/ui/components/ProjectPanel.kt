package dev.inkide.ui.components

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.inkide.core.project.ProjectNode
import dev.inkide.core.project.ProjectState
import dev.inkide.ui.IdeDimensions
import dev.inkide.ui.InkColors
import java.nio.file.Path

@Composable
fun ProjectPanel(
    state: ProjectState,
    onFileSelected: (Path) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(InkColors.DarkGraphite)
            .padding(
                IdeDimensions.PanelPadding,
            ),
    ) {
        Text(
            text = "PROJECT",
            color = InkColors.TextMuted,
            fontSize = 12.sp,
        )

        Spacer(
            modifier = Modifier.width(1.dp),
        )

        when {
            state.loading -> {
                Text(
                    text = "Loading...",
                    color = InkColors.TextMuted,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(
                        top = 12.dp,
                    ),
                )
            }

            state.error != null -> {
                Text(
                    text = state.error,
                    color = InkColors.Orange,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(
                        top = 12.dp,
                    ),
                    softWrap = false,
                    maxLines = 1,
                )
            }

            state.rootNode != null -> {
                ProjectTree(
                    rootNode = state.rootNode,
                    onFileSelected = onFileSelected,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(
                            top = 12.dp,
                        ),
                )
            }

            else -> {
                Text(
                    text = "No project open",
                    color = InkColors.TextMuted,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(
                        top = 12.dp,
                    ),
                )
            }
        }
    }
}

@Composable
private fun ProjectTree(
    rootNode: ProjectNode,
    onFileSelected: (Path) -> Unit,
    modifier: Modifier = Modifier,
) {
    val verticalScrollState =
        rememberScrollState()

    val horizontalScrollState =
        rememberScrollState()

    Box(
        modifier = modifier,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    verticalScrollState,
                )
                .horizontalScroll(
                    horizontalScrollState,
                ),
        ) {
            ProjectTreeNode(
                node = rootNode,
                depth = 0,
                onFileSelected = onFileSelected,
            )
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(
                verticalScrollState,
            ),
            modifier = Modifier
                .align(
                    Alignment.CenterEnd,
                )
                .fillMaxHeight(),
        )

        HorizontalScrollbar(
            adapter = rememberScrollbarAdapter(
                horizontalScrollState,
            ),
            modifier = Modifier
                .align(
                    Alignment.BottomStart,
                )
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun ProjectTreeNode(
    node: ProjectNode,
    depth: Int,
    onFileSelected: (Path) -> Unit,
) {
    var expanded by remember(
        node.path,
    ) {
        mutableStateOf(
            depth < 2,
        )
    }

    Row(
        modifier = Modifier
            .clickable {
                if (node.isDirectory) {
                    expanded = !expanded
                } else {
                    onFileSelected(
                        node.path,
                    )
                }
            }
            .padding(
                vertical = 3.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(
            modifier = Modifier.width(
                (depth * 14).dp,
            ),
        )

        Text(
            text = when {
                !node.isDirectory -> " "
                expanded -> "▼"
                else -> "▶"
            },
            color = InkColors.TextMuted,
            fontSize = 13.sp,
            softWrap = false,
            maxLines = 1,
        )

        Spacer(
            modifier = Modifier.width(
                6.dp,
            ),
        )

        Text(
            text = node.name,
            color = if (node.isDirectory) {
                InkColors.TextPrimary
            } else {
                InkColors.TextMuted
            },
            fontSize = 13.sp,
            softWrap = false,
            maxLines = 1,
        )
    }

    if (
        node.isDirectory &&
        expanded
    ) {
        node.children.forEach { child ->
            ProjectTreeNode(
                node = child,
                depth = depth + 1,
                onFileSelected = onFileSelected,
            )
        }
    }
}