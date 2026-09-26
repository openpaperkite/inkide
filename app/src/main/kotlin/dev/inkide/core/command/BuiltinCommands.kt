package dev.inkide.core.command

object BuiltinCommands {

    val Save =
        CommandId(
            "file.save",
        )

    val CloseFile =
        CommandId(
            "file.close",
        )

    val OpenProject =
        CommandId(
            "project.open",
        )

    val NewProject =
        CommandId(
            "project.new",
        )
}