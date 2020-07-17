package com.example.notekeeper

import android.os.Bundle
import androidx.lifecycle.ViewModel

class NoteActivityViewModel : ViewModel() {

    var mOriginalNoteCourseId: String? = null
    var mOriginalNoteTitle: String? = null
    var mOriginalNoteText: String? = null
    var mIsNewlyCreated = true

    fun saveState(outState: Bundle) {
        outState.putString(ORIGINAL_NOTE_COURSE_ID, mOriginalNoteCourseId)
        outState.putString(ORIGINAL_NOTE_TITLE, mOriginalNoteTitle)
        outState.putString(ORIGINAL_NOTE_TEXT, mOriginalNoteText)
    }

    fun restoreState(inState: Bundle) {
        mOriginalNoteCourseId = inState.getString(ORIGINAL_NOTE_COURSE_ID)
        mOriginalNoteTitle = inState.getString(ORIGINAL_NOTE_TEXT)
        mOriginalNoteText = inState.getString(ORIGINAL_NOTE_TITLE)
    }

    companion object {
        val ORIGINAL_NOTE_COURSE_ID = "com.example.notekeeper.ORIGINAL_NOTE_COURSE_ID"
        val ORIGINAL_NOTE_TITLE = "com.example.notekeeper.ORIGINAL_NOTE_TITLE"
        val ORIGINAL_NOTE_TEXT = "com.example.notekeeper.ORIGINAL_NOTE_TEXT"
    }
}