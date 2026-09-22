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
import androidx.compose.runtime.LaunchedEffect
import dev.inkide.core.filesystem.LocalFileSystem
import dev.inkide.core.project.ProjectService
import java.nio.file.Paths

@Composable
fun ApplicationScope.IdeApplication() {

    val workspace = remember {
        DefaultWorkspace()
    }

    val fileSystem = remember {
        LocalFileSystem()
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

    LaunchedEffect(projectService) {
        val projectPath =
            Paths.get("")
                .toAbsolutePath()
                .normalize()

        projectService.openProject(
            projectPath,
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
                modifier = Modifier.fillMaxSize(),
            ) {
                IdeMenuBar()

                Workbench(
                    workspace = workspace,
                    projectService = projectService,
                    fileDocumentLoader = fileDocumentLoader,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                )
            }
        }
    }
}