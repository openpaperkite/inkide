package dev.inkide.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import dev.inkide.ui.IdeDimensions
import dev.inkide.ui.InkColors

@Composable
fun IdeMenuBar(
    onOpenProject: () -> Unit,
    onNewProject: () -> Unit,
) {
    var fileMenuExpanded by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(
                IdeDimensions.MenuBarHeight,
            )
            .background(
                InkColors.Graphite,
            )
            .padding(
                horizontal =
                    IdeDimensions.PanelPadding,
            ),
        verticalAlignment =
            Alignment.CenterVertically,
        horizontalArrangement =
            Arrangement.spacedBy(20.dp),
    ) {

        Box {
            Text(
                text = "File",
                color = InkColors.TextPrimary,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable {
                        fileMenuExpanded = true
                    },
            )

            DropdownMenu(
                expanded = fileMenuExpanded,
                onDismissRequest = {
                    fileMenuExpanded = false
                },
            ) {

                DropdownMenuItem(
                    onClick = {
                        fileMenuExpanded = false
                        onNewProject()
                    },
                ) {
                    Text(
                        text = "New Project",
                    )
                }

                DropdownMenuItem(
                    onClick = {
                        fileMenuExpanded = false
                        onOpenProject()
                    },
                ) {
                    Text(
                        text = "Open Project",
                    )
                }
            }
        }

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