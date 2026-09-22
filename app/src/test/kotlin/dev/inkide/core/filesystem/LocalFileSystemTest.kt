package dev.inkide.core.filesystem

import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LocalFileSystemTest {

    @Test
    fun `children returns directory contents`() =
        runBlocking {

            val root =
                Files.createTempDirectory(
                    "inkide-test",
                )

            try {
                Files.createDirectory(
                    root.resolve("src"),
                )

                Files.writeString(
                    root.resolve("Main.kt"),
                    "fun main() {}",
                )

                val fileSystem =
                    LocalFileSystem()

                val children =
                    fileSystem.children(root)

                assertEquals(
                    2,
                    children.size,
                )

                assertTrue(
                    children.any {
                        it.fileName.toString() ==
                                "src"
                    },
                )

                assertTrue(
                    children.any {
                        it.fileName.toString() ==
                                "Main.kt"
                    },
                )
            } finally {
                root
                    .toFile()
                    .deleteRecursively()
            }
        }
}