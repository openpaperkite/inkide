package dev.inkide.core.config

import java.nio.file.Path
import java.nio.file.Paths

object AppDirectories {

    val projectsDirectory: Path
        get() {
            val home =
                Paths.get(
                    System.getProperty("user.home"),
                )

            return home.resolve("InkProjects")
        }

    val configDirectory: Path
        get() {
            val osName =
                System.getProperty("os.name")
                    .lowercase()

            val home =
                Paths.get(
                    System.getProperty("user.home"),
                )

            return when {
                osName.contains("mac") ->
                    home.resolve(
                        "Library/Application Support/InkIDE",
                    )

                osName.contains("win") -> {
                    val appData =
                        System.getenv("APPDATA")

                    if (appData != null) {
                        Paths.get(appData)
                            .resolve("InkIDE")
                    } else {
                        home.resolve(".inkide")
                    }
                }

                else -> {
                    val xdg =
                        System.getenv(
                            "XDG_CONFIG_HOME",
                        )

                    if (xdg != null) {
                        Paths.get(xdg)
                            .resolve("inkide")
                    } else {
                        home.resolve(
                            ".config/inkide",
                        )
                    }
                }
            }
        }
}