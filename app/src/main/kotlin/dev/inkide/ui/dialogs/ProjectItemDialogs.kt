package dev.inkide.ui.dialogs

import javax.swing.JOptionPane

fun requestFileName(): String? {
    return JOptionPane.showInputDialog(
        null,
        "File name:",
        "New File",
        JOptionPane.PLAIN_MESSAGE,
    )
        ?.trim()
        ?.takeIf {
            it.isNotEmpty()
        }
}

fun requestDirectoryName(): String? {
    return JOptionPane.showInputDialog(
        null,
        "Directory name:",
        "New Directory",
        JOptionPane.PLAIN_MESSAGE,
    )
        ?.trim()
        ?.takeIf {
            it.isNotEmpty()
        }
}