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
import dev.inkide.ui.IdeDimensions

@Composable
fun TerminalArea(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.background(InkColors.DarkGraphite),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IdeDimensions.TerminalTabBarHeight)
                .background(InkColors.LightGraphite)
                .padding(horizontal = IdeDimensions.PanelPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Terminal 1",
                color = InkColors.Orange,
                fontSize = 14.sp,
            )

            Text(
                text = "Terminal 2",
                color = InkColors.TextMuted,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 24.dp),
            )

            Text(
                text = "+",
                color = InkColors.TextPrimary,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 24.dp),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(InkColors.DarkGraphite),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "TERMINAL",
                color = InkColors.TextMuted,
                fontSize = 18.sp,
            )
        }
    }
}