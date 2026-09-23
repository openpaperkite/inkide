package dev.inkide.core.document

import dev.inkide.core.filesystem.LocalFileSystem
import kotlinx.coroutines.runBlocking
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FileDocumentLoaderTest {

    @Test
    fun `load creates document from file`() =
        runBlocking {

            val directory =
                Files.createTempDirectory(
                    "inkide-document-test",
                )

            val file =
                directory.resolve(
                    "Main.kt",
                )

            try {
                Files.writeString(
                    file,
                    "fun main() {}",
                )

                val loader =
                    FileDocumentLoader(
                        LocalFileSystem(),
                    )

                val document =
                    loader.load(file)

                assertEquals(
                    "Main.kt",
                    document.name,
                )

                assertEquals(
                    "fun main() {}",
                    document.state.value.content,
                )

                assertFalse(
                    document.state.value.isModified,
                )

                assertTrue(
                    document.id.value
                        .startsWith("file:"),
                )
            } finally {
                directory
                    .toFile()
                    .deleteRecursively()
            }
        }
}