package dev.inkide.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import dev.inkide.ui.IdeDimensions
import dev.inkide.ui.InkColors
import java.awt.Cursor

@Composable
fun VerticalSplitter(
    onDrag: (Float) -> Unit,
) {
    var dragging by remember {
        mutableStateOf(false)
    }

    val lineColor =
        if (dragging) {
            InkColors.Green
        } else {
            InkColors.LightGraphite
        }

    Box(
        modifier = Modifier
            .width(IdeDimensions.SplitterHitArea)
            .fillMaxHeight()
            .pointerHoverIcon(
                PointerIcon(
                    Cursor.getPredefinedCursor(
                        Cursor.E_RESIZE_CURSOR,
                    ),
                ),
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        dragging = true
                    },
                    onDragEnd = {
                        dragging = false
                    },
                    onDragCancel = {
                        dragging = false
                    },
                ) { change, dragAmount ->
                    change.consume()

                    onDrag(
                        dragAmount.x,
                    )
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .width(IdeDimensions.SplitterLineThickness)
                .fillMaxHeight()
                .background(lineColor),
        )
    }
}

@Composable
fun HorizontalSplitter(
    onDrag: (Float) -> Unit,
) {
    var dragging by remember {
        mutableStateOf(false)
    }

    val lineColor =
        if (dragging) {
            InkColors.Green
        } else {
            InkColors.LightGraphite
        }

    Box(
        modifier = Modifier
            .height(IdeDimensions.SplitterHitArea)
            .fillMaxWidth()
            .pointerHoverIcon(
                PointerIcon(
                    Cursor.getPredefinedCursor(
                        Cursor.S_RESIZE_CURSOR,
                    ),
                ),
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        dragging = true
                    },
                    onDragEnd = {
                        dragging = false
                    },
                    onDragCancel = {
                        dragging = false
                    },
                ) { change, dragAmount ->
                    change.consume()

                    onDrag(
                        dragAmount.y,
                    )
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .height(IdeDimensions.SplitterLineThickness)
                .fillMaxWidth()
                .background(lineColor),
        )
    }
}