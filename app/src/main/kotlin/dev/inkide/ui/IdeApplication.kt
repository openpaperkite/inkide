package dev.inkide.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import dev.inkide.core.command.*
import dev.inkide.core.config.AppDirectories
import dev.inkide.core.config.AppDirectoriesInitializer
import dev.inkide.core.document.DocumentSaver
import dev.inkide.core.document.FileDocumentLoader
import dev.inkide.core.filesystem.LocalFileSystem
import dev.inkide.core.project.ProjectManager
import dev.inkide.core.project.ProjectMetadataStore
import dev.inkide.core.project.ProjectService
import dev.inkide.core.session.ApplicationSessionStore
import dev.inkide.core.workspace.DefaultWorkspace
import dev.inkide.ui.components.IdeMenuBar
import dev.inkide.ui.dialogs.chooseProjectDirectory
import dev.inkide.ui.dialogs.requestNewProject
import dev.inkide.ui.workbench.Workbench
import kotlinx.coroutines.launch

@Composable
fun ApplicationScope.IdeApplication() {

    val fileSystem = remember {
        LocalFileSystem()
    }

    val workspace = remember {
        DefaultWorkspace()
    }

    val projectService = remember {
        ProjectService(
            fileSystem = fileSystem,
        )
    }

    val fileDocumentLoader = remember {
        FileDocumentLoader(
            fileSystem = fileSystem,
        )
    }

    val documentSaver = remember {
        DocumentSaver(
            fileSystem = fileSystem,
        )
    }

    val sessionStore = remember {
        ApplicationSessionStore()
    }

    val metadataStore = remember {
        ProjectMetadataStore()
    }

    val projectManager = remember {
        ProjectManager(
            projectService = projectService,
            workspace = workspace,
            sessionStore = sessionStore,
            metadataStore = metadataStore,
        )
    }

    val scope = rememberCoroutineScope()

    val directoriesInitializer = remember {
        AppDirectoriesInitializer()
    }

    val commandRegistry = remember {
        CommandRegistry()
    }

    val keybindingRegistry =
        remember {
            KeybindingRegistry(
                commandRegistry = commandRegistry,
            )
        }

    // [ BEGIN OF KEY EVENTS]
    keybindingRegistry.register(
        Keybinding(
            key = Key.S,
            meta = true,
            commandId =
                BuiltinCommands.Save,
        ),
    )
    // [ END OF KEY EVENTS ]

    LaunchedEffect(projectManager) {
        directoriesInitializer.initialize()

        projectManager.restoreLastProject()
    }

    LaunchedEffect(
        commandRegistry,
        workspace,
        documentSaver,
    ) {
        commandRegistry.register(
            Command(
                id = BuiltinCommands.Save,
                label = "Save",
                execute = {
                    val document =
                        workspace.state.value
                            .activeDocument
                            ?: return@Command

                    scope.launch {
                        documentSaver.save(
                            document,
                        )
                    }
                },
            ),
        )
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Ink IDE",
        state = WindowState(
            width = IdeDimensions.DefaultWindowWidth,
            height = IdeDimensions.DefaultWindowHeight,
        ),
    ) {
        IdeTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .onPreviewKeyEvent { event ->
                        keybindingRegistry.handle(
                            event,
                        )
                    },
            ) {
                IdeMenuBar(
                    onOpenProject = {
                        val directory =
                            chooseProjectDirectory()

                        if (directory != null) {
                            scope.launch {
                                projectManager.openProject(
                                    directory,
                                )
                            }
                        }
                    },
                    onNewProject = {
                        val request =
                            requestNewProject()

                        if (request != null) {
                            scope.launch {
                                projectManager.createProject(
                                    parentDirectory =
                                        AppDirectories.projectsDirectory,
                                    projectName =
                                        request.projectName,
                                )
                            }
                        }
                    },

                    onSave = {
                        commandRegistry.execute(
                            BuiltinCommands.Save,
                        )
                    },
                )

                Workbench(
                    workspace = workspace,
                    projectService = projectService,
                    fileDocumentLoader = fileDocumentLoader,
                    documentSaver = documentSaver,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                )
            }
        }
    }
}