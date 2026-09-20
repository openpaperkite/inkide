package dev.inkide.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.WindowPosition
import dev.inkide.ui.components.IdeMenuBar
import dev.inkide.ui.workbench.Workbench
import dev.inkide.ui.IdeDimensions

@Composable
fun ApplicationScope.IdeApplication() {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Ink IDE",
        state = WindowState(
            width = IdeDimensions.DefaultWindowWidth,
            height = IdeDimensions.DefaultWindowHeight,
            position = WindowPosition.Aligned(
                alignment = androidx.compose.ui.Alignment.Center,
            ),
        ),
    ){
        IdeTheme{
            Column(
                modifier = Modifier.fillMaxSize(),
            ){
                IdeMenuBar()
                Workbench(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                )
            }
        }
    }
}