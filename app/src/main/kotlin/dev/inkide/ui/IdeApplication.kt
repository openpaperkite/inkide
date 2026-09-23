package dev.inkide.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import dev.inkide.core.document.FileDocumentLoader
import dev.inkide.core.workspace.DefaultWorkspace
import dev.inkide.core.workspace.Workspace
import dev.inkide.ui.components.IdeMenuBar
import dev.inkide.ui.workbench.Workbench
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import dev.inkide.core.filesystem.LocalFileSystem
import dev.inkide.core.document.DocumentSaver
import dev.inkide.core.config.AppDirectories
import dev.inkide.core.project.ProjectService
import dev.inkide.core.project.ProjectManager
import dev.inkide.core.project.ProjectMetadataStore
import dev.inkide.core.session.ApplicationSessionStore
import dev.inkide.core.config.AppDirectoriesInitializer
import dev.inkide.ui.dialogs.chooseProjectDirectory
import dev.inkide.ui.dialogs.requestNewProject
import java.nio.file.Paths

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

    LaunchedEffect(projectManager) {
        directoriesInitializer.initialize()

        projectManager.restoreLastProject()
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
                modifier = Modifier.fillMaxSize(),
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
                        val document =
                            workspace.state.value
                                .activeDocument

                        if (document != null) {
                            scope.launch {
                                documentSaver.save(
                                    document,
                                )
                            }
                        }
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