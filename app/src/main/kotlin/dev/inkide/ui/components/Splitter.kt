package dev.inkide.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import dev.inkide.ui.InkColors

@Composable
fun VerticalSplitter(
    onDrag: (Float) -> Unit,
) {
    Box(
        modifier = Modifier
            .width(6.dp)
            .fillMaxHeight()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onDrag(dragAmount.x)
                }
            },
    ) {
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(InkColors.LightGraphite),
        )
    }
}

@Composable
fun HorizontalSplitter(
    onDrag: (Float) -> Unit,
) {
    Box(
        modifier = Modifier
            .height(6.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onDrag(dragAmount.y)
                }
            },
    ) {
        Box(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(InkColors.LightGraphite),
        )
    }
}