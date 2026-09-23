package dev.inkide.ui.dialogs

import dev.inkide.core.document.CloseDecision
import dev.inkide.core.document.Document
import javax.swing.JOptionPane

fun requestCloseDecision(
    document: Document,
): CloseDecision {
    val result =
        JOptionPane.showOptionDialog(
            null,
            "Save changes to ${document.name}?",
            "Unsaved Changes",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            arrayOf(
                "Save",
                "Don't Save",
                "Cancel",
            ),
            "Save",
        )

    return when (result) {
        0 -> CloseDecision.SAVE
        1 -> CloseDecision.DISCARD
        else -> CloseDecision.CANCEL
    }
}