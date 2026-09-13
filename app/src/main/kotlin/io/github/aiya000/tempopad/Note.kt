package io.github.aiya000.tempopad

import android.content.Context
import androidx.core.content.edit

private const val PreferencesName = "tempo_pad"
private const val NoteKey = "note"

/** The text the pad was left holding, or an empty string on the very first launch. */
fun Context.readNote(): String = notePreferences().getString(NoteKey, null).orEmpty()

fun Context.writeNote(text: String) {
    notePreferences().edit { putString(NoteKey, text) }
}

private fun Context.notePreferences() =
    getSharedPreferences(PreferencesName, Context.MODE_PRIVATE)
