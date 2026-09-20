package dev.inkide.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
fun IdeMenuBar(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(InkColors.Graphite)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = "File",
            color = InkColors.TextPrimary,
            fontSize = 14.sp,
        )

        Text(
            text = "Edit",
            color = InkColors.TextPrimary,
            fontSize = 14.sp,
        )

        Text(
            text = "View",
            color = InkColors.TextPrimary,
            fontSize = 14.sp,
        )

        Text(
            text = "Run",
            color = InkColors.TextPrimary,
            fontSize = 14.sp,
        )
    }
}