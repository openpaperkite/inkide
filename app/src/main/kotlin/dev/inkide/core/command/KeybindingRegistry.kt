package dev.inkide.core.command

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.key.key

class KeybindingRegistry(
    private val commandRegistry: CommandRegistry,
) {

    private val keybindings =
        mutableListOf<Keybinding>()

    fun register(
        keybinding: Keybinding,
    ) {
        keybindings += keybinding
    }

    fun handle(
        event: KeyEvent,
    ): Boolean {
        if (event.type != KeyEventType.KeyDown) {
            return false
        }

        val keybinding =
            keybindings.firstOrNull {
                it.matches(event)
            } ?: return false

        return commandRegistry.execute(
            keybinding.commandId,
        )
    }

    private fun Keybinding.matches(
        event: KeyEvent,
    ): Boolean {
        return key == event.key &&
                meta == event.isMetaPressed &&
                ctrl == event.isCtrlPressed &&
                alt == event.isAltPressed &&
                shift == event.isShiftPressed
    }
}