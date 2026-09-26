package dev.inkide.core.command

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CommandRegistryTest {

    @Test
    fun `registered command executes`() {
        val registry =
            CommandRegistry()

        var executed = false

        val command =
            Command(
                id = CommandId(
                    "test.command",
                ),
                label = "Test",
                execute = {
                    executed = true
                },
            )

        registry.register(
            command,
        )

        assertTrue(
            registry.execute(
                command.id,
            ),
        )

        assertTrue(
            executed,
        )
    }

    @Test
    fun `unknown command does not execute`() {
        val registry =
            CommandRegistry()

        assertFalse(
            registry.execute(
                CommandId(
                    "missing.command",
                ),
            ),
        )
    }
}