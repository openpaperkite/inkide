package dev.inkide.ui.workbench

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import dev.inkide.ui.components.EditorArea
import dev.inkide.ui.components.HorizontalSplitter
import dev.inkide.ui.components.ProjectPanel
import dev.inkide.ui.components.TerminalArea
import dev.inkide.ui.components.VerticalSplitter

@Composable
fun Workbench(
    modifier: Modifier = Modifier,
) {
    var projectWidth by remember {
        mutableStateOf(240.dp)
    }

    var terminalHeight by remember {
        mutableStateOf(220.dp)
    }

    val density = LocalDensity.current

    Row(
        modifier = modifier.fillMaxSize(),
    ) {
        ProjectPanel(
            modifier = Modifier
                .width(projectWidth)
                .fillMaxHeight(),
        )

        VerticalSplitter(
            onDrag = { deltaPixels ->
                val deltaDp = with(density) {
                    deltaPixels.toDp()
                }

                projectWidth = (projectWidth + deltaDp)
                    .coerceIn(
                        minimumValue = 160.dp,
                        maximumValue = 500.dp,
                    )
            },
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
        ) {
            EditorArea(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )

            HorizontalSplitter(
                onDrag = { deltaPixels ->
                    val deltaDp = with(density) {
                        deltaPixels.toDp()
                    }

                    terminalHeight = (terminalHeight - deltaDp)
                        .coerceIn(
                            minimumValue = 100.dp,
                            maximumValue = 500.dp,
                        )
                },
            )

            TerminalArea(
                modifier = Modifier
                    .height(terminalHeight)
                    .fillMaxWidth(),
            )
        }
    }
}