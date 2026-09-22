package dev.inkide.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import dev.inkide.core.document.DocumentId
import dev.inkide.core.document.TextDocument
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
        createDemoWorkspace()
    }

    val projectService = remember {
        ProjectService(
            fileSystem = LocalFileSystem(),
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
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                )
            }
        }
    }
}

private fun createDemoWorkspace(): Workspace {
    return DefaultWorkspace().apply {

        openDocument(
            TextDocument(
                id = DocumentId("main.kt"),
                name = "Main.kt",
                initialContent = """
                    fun main() {
                        println("Hello from Ink IDE")
                    }
                """.trimIndent(),
            ),
        )

        openDocument(
            TextDocument(
                id = DocumentId("app.kt"),
                name = "App.kt",
                initialContent = """
                    class App {
                        fun start() {
                            println("Starting application...")
                        }
                    }
                """.trimIndent(),
            ),
        )

        openDocument(
            TextDocument(
                id = DocumentId("readme.md"),
                name = "README.md",
                initialContent = """
                    # Ink IDE
                    
                    A small, extensible IDE written in Kotlin.
                """.trimIndent(),
            ),
        )

        activateDocument(
            DocumentId("main.kt"),
        )
    }
}