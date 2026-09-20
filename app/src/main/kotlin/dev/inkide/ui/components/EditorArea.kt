package dev.inkide.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.inkide.ui.InkColors

@Composable
fun EditorArea(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.background(InkColors.Graphite),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(InkColors.LightGraphite)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Main.kt",
                color = InkColors.Green,
                fontSize = 14.sp,
            )

            Text(
                text = "App.kt",
                color = InkColors.TextMuted,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 24.dp),
            )

            Text(
                text = "README.md",
                color = InkColors.TextMuted,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 24.dp),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(InkColors.Graphite),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "EDITOR",
                color = InkColors.TextMuted,
                fontSize = 18.sp,
            )
        }
    }
}