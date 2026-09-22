package dev.inkide.ui.dialogs

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
    val parentDirectory: Path,
    val projectName: String,
)

fun requestNewProject(): NewProjectRequest? {

    val chooser =
        JFileChooser().apply {
            dialogTitle =
                "Choose Project Location"

            fileSelectionMode =
                JFileChooser.DIRECTORIES_ONLY

            isAcceptAllFileFilterUsed =
                false
        }

    val result =
        chooser.showOpenDialog(null)

    if (
        result !=
        JFileChooser.APPROVE_OPTION
    ) {
        return null
    }

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
        parentDirectory =
            chooser
                .selectedFile
                .toPath(),
        projectName = projectName,
    )
}