package io.github.aiya000.tempopad

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/** Colors carried over from the CopyMenu editing panel this pad is modelled on. */
object TempoPadColors {
    val PanelInner = Color(0xFFD7D5EE)
    val OnSurface = Color(0xFF16161F)
    val Hint = Color(0xFF6E6C86)
    val Cursor = Color(0xFF3A3A55)
    val Scrim = Color(0x66000000)
}

/** Gradient used by the panel border. */
val PanelBrush = Brush.linearGradient(
    colors = listOf(Color(0xFF8490E4), Color(0xFFB292DC)),
)
