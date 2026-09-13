package io.github.aiya000.tempopad

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** The gradient border drawn around the panel. */
private val BorderWidth = 20.dp

/**
 * The whole app: one text area floating over whatever is behind it.
 *
 * Nothing is committed anywhere. The text goes to [onTextChanged] as it is typed, and
 * the activity writes it out when it is paused.
 *
 * Tapping outside of the panel finishes the app, the same as the back gesture. The outer
 * half of the gradient border counts as outside as well, so that a thumb aimed at the edge
 * of the panel still closes the app when it lands a little short.
 *
 * The panel keeps the same size whether the keyboard is shown or not. Instead of
 * shrinking the panel, the text area is padded at the bottom by the height the keyboard
 * hides, so that the last line can still be brought above the keyboard.
 */
@Composable
fun TempoPadScreen(
    initialText: String,
    onTextChanged: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var value by remember {
        mutableStateOf(TextFieldValue(initialText, TextRange(initialText.length)))
    }

    val density = LocalDensity.current
    var windowHeight by remember { mutableIntStateOf(0) }
    var textAreaBottom by remember { mutableFloatStateOf(0f) }
    val keyboardTop = windowHeight - WindowInsets.ime.getBottom(density)
    val hiddenByKeyboard = with(density) {
        (textAreaBottom - keyboardTop).coerceAtLeast(0f).toDp()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { windowHeight = it.size.height }
            .background(TempoPadColors.Scrim)
            .noRippleClickable(onDismiss)
            .systemBarsPadding()
            .padding(start = 22.dp, top = 44.dp, end = 22.dp, bottom = 32.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        // The border is split in two so that its outer half dismisses like the scrim does.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.78f)
                .clip(RoundedCornerShape(30.dp))
                .background(PanelBrush)
                .noRippleClickable(onDismiss)
                .padding(BorderWidth / 2),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .noRippleClickable { }
                    .padding(BorderWidth / 2)
                    .clip(RoundedCornerShape(22.dp))
                    .background(TempoPadColors.PanelInner)
                    .onGloballyPositioned {
                        textAreaBottom = it.positionInWindow().y + it.size.height
                    }
                    .padding(14.dp),
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = {
                        value = it
                        onTextChanged(it.text)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = hiddenByKeyboard),
                    textStyle = TextStyle(
                        color = TempoPadColors.OnSurface,
                        fontSize = 16.sp,
                        lineHeight = 23.sp,
                    ),
                    cursorBrush = SolidColor(TempoPadColors.Cursor),
                )
            }
        }
    }
}

/** A [clickable] without the ripple, for backgrounds that only need to catch taps. */
@Composable
private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = this.clickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = null,
    onClick = onClick,
)
