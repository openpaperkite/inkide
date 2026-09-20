package dev.inkide.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState

@Composable
fun ApplicationScope.IdeApplication() {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Ink IDE",
        state = WindowState(
            width = 1200.dp,
            height = 800.dp,
            position = WindowPosition.Aligned(
                alignment = androidx.compose.ui.Alignment.Center,
            ),
        ),
    ){
        IdeTheme{
            Box(
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}