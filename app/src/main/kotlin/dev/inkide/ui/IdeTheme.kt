package dev.inkide.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


object InkColors {
    val Graphite = Color(0xFF2B2D30)
    val DarkGraphite = Color(0xFF1E1F22)
    val LightGraphite = Color(0xFF3A3D41)

    val Green = Color(0xFF50C878)
    val Orange = Color(0xFFE8903A)

    val TextPrimary = Color(0xFFDFE1E5)
    val TextMuted = Color(0xFF9DA1A8)
}

@Composable
fun IdeTheme(
    content : @Composable () -> Unit,
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(InkColors.DarkGraphite)
    ){
        content()
    }
}