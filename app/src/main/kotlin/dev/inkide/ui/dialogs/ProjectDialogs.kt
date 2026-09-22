package dev.inkide.ui.dialogs

import dev.inkide.core.config.AppDirectories
import java.nio.file.Path
import javax.swing.JFileChooser
import javax.swing.JOptionPane

fun chooseProjectDirectory(): Path? {
    val chooser =
        JFileChooser().apply {
            dialogTitle = "Open Project"

            fileSelectionMode =
                JFileChooser.DIRECTORIES_ONLY

            isAcceptAllFileFilterUsed =
                false

            currentDirectory =
                AppDirectories
                    .projectsDirectory
                    .toFile()
        }

    val result =
        chooser.showOpenDialog(null)

    if (
        result !=
        JFileChooser.APPROVE_OPTION
    ) {
        return null
    }

    return chooser
        .selectedFile
        .toPath()
}

data class NewProjectRequest(
    val projectName: String,
)

fun requestNewProject(): NewProjectRequest? {
    val projectName =
        JOptionPane.showInputDialog(
            null,
            "Project name:",
            "New Project",
            JOptionPane.PLAIN_MESSAGE,
        )
            ?.trim()
            ?.takeIf {
                it.isNotEmpty()
            }
            ?: return null

    return NewProjectRequest(
        projectName = projectName,
    )
}