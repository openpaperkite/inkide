package dev.inkide.ui.workbench

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.inkide.ui.InkColors
import dev.inkide.ui.components.EditorArea
import dev.inkide.ui.components.ProjectPanel
import dev.inkide.ui.components.TerminalArea

@Composable
fun Workbench(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxSize(),
    ) {
        ProjectPanel(
            modifier = Modifier
                .width(240.dp)
                .fillMaxHeight(),
        )

        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(InkColors.LightGraphite),
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

            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(InkColors.LightGraphite),
            )

            TerminalArea(
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxWidth(),
            )
        }
    }
}