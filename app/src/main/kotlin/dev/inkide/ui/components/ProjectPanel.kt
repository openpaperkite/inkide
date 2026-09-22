package dev.inkide.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.inkide.core.project.ProjectNode
import dev.inkide.core.project.ProjectState
import dev.inkide.ui.IdeDimensions
import dev.inkide.ui.InkColors

@Composable
fun ProjectPanel(
    state: ProjectState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(
                IdeDimensions.PanelPadding,
            ),
    ) {
        Text(
            text = "PROJECT",
            color = InkColors.TextMuted,
            fontSize = 12.sp,
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
                )
            }

            state.rootNode != null -> {
                Column(
                    modifier = Modifier
                        .padding(
                            top = 12.dp,
                        )
                        .verticalScroll(
                            rememberScrollState(),
                        ),
                ) {
                    ProjectTreeNode(
                        node = state.rootNode,
                        depth = 0,
                    )
                }
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
private fun ProjectTreeNode(
    node: ProjectNode,
    depth: Int,
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
            .fillMaxWidth()
            .clickable(
                enabled = node.isDirectory,
            ) {
                expanded = !expanded
            }
            .padding(
                vertical = 3.dp,
            ),
    ) {
        Spacer(
            modifier = Modifier.width(
                (depth * 14).dp,
            ),
        )

        Text(
            text = when {
                !node.isDirectory -> "  "
                expanded -> "▼"
                else -> "▶"
            },
            color = InkColors.TextMuted,
            fontSize = 13.sp,
        )

        Spacer(
            modifier = Modifier.width(6.dp),
        )

        Text(
            text = node.name,
            color = if (node.isDirectory) {
                InkColors.TextPrimary
            } else {
                InkColors.TextMuted
            },
            fontSize = 13.sp,
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
            )
        }
    }
}