package dev.inkide.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.inkide.ui.InkColors
import dev.inkide.ui.IdeDimensions

@Composable
fun ProjectPanel(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(InkColors.DarkGraphite)
            .padding(IdeDimensions.PanelPadding),
    ) {
        Text(
            text = "PROJECT",
            color = InkColors.TextMuted,
            fontSize = 12.sp,
        )

        Text(
            text = "▼ ink-ide",
            color = InkColors.TextPrimary,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 12.dp),
        )

        Text(
            text = "  ▼ src",
            color = InkColors.TextPrimary,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 6.dp),
        )

        Text(
            text = "      Main.kt",
            color = InkColors.TextPrimary,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 6.dp),
        )

        Text(
            text = "      IdeApplication.kt",
            color = InkColors.TextPrimary,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 6.dp),
        )

        Text(
            text = "  build.gradle.kts",
            color = InkColors.TextPrimary,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 6.dp),
        )

        Text(
            text = "  settings.gradle.kts",
            color = InkColors.TextPrimary,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 6.dp),
        )
    }
}