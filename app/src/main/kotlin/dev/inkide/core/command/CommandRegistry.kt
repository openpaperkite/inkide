package dev.inkide.core.command

class CommandRegistry {

    private val commands =
        mutableMapOf<CommandId, Command>()

    fun register(
        command: Command,
    ) {
        require(
            !commands.containsKey(command.id),
        ) {
            "Command already registered: ${command.id.value}"
        }

        commands[command.id] =
            command
    }

    fun execute(
        id: CommandId,
    ): Boolean {
        val command =
            commands[id]
                ?: return false

        command.execute()

        return true
    }

    fun find(
        id: CommandId,
    ): Command? =
        commands[id]
}