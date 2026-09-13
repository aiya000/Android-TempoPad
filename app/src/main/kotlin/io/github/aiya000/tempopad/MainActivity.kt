package io.github.aiya000.tempopad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.core.view.WindowCompat

/**
 * The whole app: the note, and nothing else.
 *
 * The note is read once here and written back in [onPause], which covers leaving for
 * another app, the back gesture, and the tap outside of the panel alike.
 */
class MainActivity : ComponentActivity() {

    private var note = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        note = readNote()

        setContent {
            MaterialTheme {
                TempoPadScreen(
                    initialText = note,
                    onTextChanged = { note = it },
                    onDismiss = { finish() },
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        writeNote(note)
    }
}
