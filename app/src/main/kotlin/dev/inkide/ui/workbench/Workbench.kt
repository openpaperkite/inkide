package dev.inkide.ui.workbench

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import dev.inkide.core.document.FileDocumentLoader
import dev.inkide.core.project.ProjectService
import dev.inkide.core.workspace.Workspace
import dev.inkide.ui.IdeDimensions
import dev.inkide.ui.components.EditorArea
import dev.inkide.ui.components.HorizontalSplitter
import dev.inkide.ui.components.ProjectPanel
import dev.inkide.ui.components.TerminalArea
import dev.inkide.ui.components.VerticalSplitter
import dev.inkide.core.document.CloseDecision
import dev.inkide.core.document.DocumentSaver
import dev.inkide.ui.dialogs.requestCloseDecision
import kotlinx.coroutines.launch

@Composable
fun Workbench(
    workspace: Workspace,
    projectService: ProjectService,
    fileDocumentLoader: FileDocumentLoader,
    documentSaver: DocumentSaver,
    modifier: Modifier = Modifier,
) {
    val workspaceState by workspace.state.collectAsState()
    val projectState by projectService.state.collectAsState()

    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    var projectWidth by remember {
        mutableStateOf(
            IdeDimensions.ProjectPanelDefaultWidth,
        )
    }

    var terminalHeight by remember {
        mutableStateOf(
            IdeDimensions.TerminalDefaultHeight,
        )
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {

        // Upper workbench:
        //
        // Project | Editor
        //
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
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

                onCreateFile = { parent, name ->
                    scope.launch {
                        projectService.createFile(
                            parent = parent,
                            name = name,
                        )
                    }
                },

                onCreateDirectory = { parent, name ->
                    scope.launch {
                        projectService.createDirectory(
                            parent = parent,
                            name = name,
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

                    projectWidth =
                        (projectWidth + deltaDp)
                            .coerceIn(
                                minimumValue =
                                    IdeDimensions.ProjectPanelMinWidth,
                                maximumValue =
                                    IdeDimensions.ProjectPanelMaxWidth,
                            )
                },
            )

            EditorArea(
                state = workspaceState,
                onDocumentSelected = { documentId ->
                    workspace.activateDocument(
                        documentId,
                    )
                },
                onDocumentClosed = { documentId ->
                    val document =
                        workspace.state.value.documents
                            .firstOrNull {
                                it.id == documentId
                            }

                    if (document != null) {
                        val documentState =
                            document.state.value

                        if (!documentState.isModified) {
                            workspace.closeDocument(
                                documentId,
                            )
                        } else {
                            when (
                                requestCloseDecision(
                                    document,
                                )
                            ) {
                                CloseDecision.SAVE -> {
                                    scope.launch {
                                        documentSaver.save(
                                            document,
                                        )

                                        workspace.closeDocument(
                                            documentId,
                                        )
                                    }
                                }

                                CloseDecision.DISCARD -> {
                                    workspace.closeDocument(
                                        documentId,
                                    )
                                }

                                CloseDecision.CANCEL -> {
                                    // Do nothing.
                                }
                            }
                        }
                    }
                },
                onDocumentContentChanged = { documentId, content ->
                    workspace.updateDocumentContent(
                        id = documentId,
                        content = content,
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            )
        }

        // Full-width divider between the upper
        // workbench and terminal.
        HorizontalSplitter(
            onDrag = { deltaPixels ->
                val deltaDp = with(density) {
                    deltaPixels.toDp()
                }

                terminalHeight =
                    (terminalHeight - deltaDp)
                        .coerceIn(
                            minimumValue =
                                IdeDimensions.TerminalMinHeight,
                            maximumValue =
                                IdeDimensions.TerminalMaxHeight,
                        )
            },
        )

        // Terminal now spans the ENTIRE IDE width.
        TerminalArea(
            modifier = Modifier
                .height(terminalHeight)
                .fillMaxWidth(),
        )
    }
}