package dev.inkide.ui.workbench

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import dev.inkide.ui.components.EditorArea
import dev.inkide.ui.components.HorizontalSplitter
import dev.inkide.ui.components.ProjectPanel
import dev.inkide.ui.components.TerminalArea
import dev.inkide.ui.components.VerticalSplitter
import dev.inkide.ui.IdeDimensions
import dev.inkide.core.workspace.Workspace
import dev.inkide.core.project.ProjectService
import dev.inkide.core.document.FileDocumentLoader

@Composable
fun Workbench(
    workspace: Workspace,
    projectService: ProjectService,
    fileDocumentLoader: FileDocumentLoader,
    modifier: Modifier = Modifier,
) {

    val workspaceState by workspace.state.collectAsState()
    val projectState by projectService.state.collectAsState()

    val scope = rememberCoroutineScope()

    var projectWidth by remember {
        mutableStateOf(IdeDimensions.ProjectPanelDefaultWidth)
    }

    var terminalHeight by remember {
        mutableStateOf(IdeDimensions.TerminalDefaultHeight)
    }

    val density = LocalDensity.current

    Row(
        modifier = modifier.fillMaxSize(),
    ) {
        ProjectPanel(
            state = projectState,
            onFileSelected = { path ->
                scope.launch {
                    val document =
                        fileDocumentLoader.load(path)

                    workspace.openDocument(
                        document,
                    )
                }
            },
            modifier = Modifier
                .width(projectWidth)
                .fillMaxHeight(),
        )

        VerticalSplitter(
            onDrag = { deltaPixels ->
                val deltaDp = with(density) {
                    deltaPixels.toDp()
                }

                projectWidth = (projectWidth + deltaDp)
                    .coerceIn(
                        minimumValue = IdeDimensions.ProjectPanelMinWidth,
                        maximumValue = IdeDimensions.ProjectPanelMaxWidth,
                    )
            },
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        ) {
            EditorArea(
                state = workspaceState,
                onDocumentSelected = { documentId ->
                    workspace.activateDocument(
                        documentId,
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )

            HorizontalSplitter(
                onDrag = { deltaPixels ->
                    val deltaDp = with(density) {
                        deltaPixels.toDp()
                    }

                    terminalHeight = (terminalHeight - deltaDp)
                        .coerceIn(
                            minimumValue = IdeDimensions.TerminalMinHeight,
                            maximumValue = IdeDimensions.TerminalMaxHeight,
                        )
                },
            )

            TerminalArea(
                modifier = Modifier
                    .height(terminalHeight)
                    .fillMaxWidth(),
            )
        }
    }
}