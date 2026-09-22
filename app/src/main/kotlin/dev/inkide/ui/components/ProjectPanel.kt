package dev.inkide.ui.components

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
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
import dev.inkide.ui.dialogs.requestFileName
import dev.inkide.ui.dialogs.requestDirectoryName
import dev.inkide.ui.InkColors
import java.nio.file.Path

@Composable
fun ProjectPanel(
    state: ProjectState,
    onFileSelected: (Path) -> Unit,
    onCreateFile: (
        parent: Path,
        name: String,
    ) -> Unit,
    onCreateDirectory: (
        parent: Path,
        name: String,
    ) -> Unit,
    modifier: Modifier = Modifier,
) {

    var selectedPath by remember {
        mutableStateOf<Path?>(null)
    }

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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 6.dp,
                    bottom = 6.dp,
                ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "+ File",
                color = InkColors.Green,
                modifier = Modifier.clickable {
                    val parent =
                        creationParent(
                            selectedPath = selectedPath,
                            state = state,
                        )

                    if (parent != null) {
                        val name =
                            requestFileName()

                        if (name != null) {
                            onCreateFile(
                                parent,
                                name,
                            )
                        }
                    }
                },
                fontSize = 12.sp,
            )

            Text(
                text = "+ Dir",
                color = InkColors.Orange,
                modifier = Modifier.clickable {
                    val parent =
                        creationParent(
                            selectedPath = selectedPath,
                            state = state,
                        )

                    if (parent != null) {
                        val name =
                            requestDirectoryName()

                        if (name != null) {
                            onCreateDirectory(
                                parent,
                                name,
                            )
                        }
                    }
                },
                fontSize = 12.sp,
            )
        }

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
                    selectedPath = selectedPath,
                    onSelected = { path ->
                        selectedPath = path
                    },
                    onFileSelected = onFileSelected,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
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
    selectedPath: Path?,
    onSelected: (Path) -> Unit,
    onFileSelected: (Path) -> Unit,
    modifier: Modifier = Modifier,
) {
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    Box(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(verticalScrollState)
                .horizontalScroll(horizontalScrollState),
        ) {
            ProjectTreeNode(
                node = rootNode,
                depth = 0,
                selectedPath = selectedPath,
                onSelected = onSelected,
                onFileSelected = onFileSelected,
            )
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(verticalScrollState),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
        )

        HorizontalScrollbar(
            adapter = rememberScrollbarAdapter(horizontalScrollState),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(),
        )
    }
}

@Composable
private fun ProjectTreeNode(
    node: ProjectNode,
    depth: Int,
    selectedPath: Path?,
    onSelected: (Path) -> Unit,
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
                onSelected(
                    node.path,
                )

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
                selectedPath = selectedPath,
                onSelected = onSelected,
                onFileSelected = onFileSelected,
            )
        }
    }
}

private fun creationParent(
    selectedPath: Path?,
    state: ProjectState,
): Path? {
    val selected =
        selectedPath

    if (selected == null) {
        return state.rootPath
    }

    val selectedNode =
        findNode(
            node = state.rootNode,
            path = selected,
        )

    return when {
        selectedNode == null ->
            state.rootPath

        selectedNode.isDirectory ->
            selectedNode.path

        else ->
            selectedNode.path.parent
    }
}

private fun findNode(
    node: ProjectNode?,
    path: Path,
): ProjectNode? {
    if (node == null) {
        return null
    }

    if (node.path == path) {
        return node
    }

    node.children.forEach { child ->
        val result =
            findNode(
                node = child,
                path = path,
            )

        if (result != null) {
            return result
        }
    }

    return null
}