package dev.inkide.core.command

data class Command(
    val id: CommandId,
    val label: String,
    val execute: () -> Unit,
)