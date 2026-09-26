package dev.inkide.core.command

import androidx.compose.ui.input.key.Key

data class Keybinding(
    val key: Key,
    val meta: Boolean = false,
    val ctrl: Boolean = false,
    val alt: Boolean = false,
    val shift: Boolean = false,
    val commandId: CommandId,
)