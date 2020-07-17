package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider

class NoteActivity : AppCompatActivity() {
    private var mNote: NoteInfo? = null
    private var mIsNewNote: Boolean = false
    private var mSpinnerCourses: Spinner? = null
    private var mTextNoteTitle: EditText? = null
    private var mTextNoteText: EditText? = null
    private var mNotePosition: Int = 0
    private var mIsCancelling: Boolean = false
    private var mViewModel: NoteActivityViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val viewModelProvider = ViewModelProvider(
            viewModelStore,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )
        mViewModel = viewModelProvider.get(NoteActivityViewModel::class.java)

        if (mViewModel!!.mIsNewlyCreated && savedInstanceState != null)
            mViewModel!!.restoreState(savedInstanceState)
        mViewModel!!.mIsNewlyCreated = false

        mSpinnerCourses = findViewById(R.id.spinner_courses)
        val courses = DataManager.courses
        val adapterCourses = ArrayAdapter<CourseInfo>(this, android.R.layout.simple_spinner_item, courses)
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSpinnerCourses!!.adapter = adapterCourses

        readDisplayStateValues()
        saveOriginalNoteValues()

        mTextNoteTitle = findViewById(R.id.text_note_title)
        mTextNoteText = findViewById(R.id.text_note_text)

        if (!mIsNewNote)
            displayNote(mSpinnerCourses!!, mTextNoteTitle!!, mTextNoteText!!)
    }

    private fun saveOriginalNoteValues() {
        if (mIsNewNote)
            return
        mViewModel!!.mOriginalNoteCourseId = mNote!!.course!!.courseId
        mViewModel!!.mOriginalNoteTitle = mNote!!.title
        mViewModel!!.mOriginalNoteText = mNote!!.text

    }

    override fun onPause() {
        super.onPause()
        if (mIsCancelling) {
            if (mIsNewNote) {
                DataManager.removeNote(mNotePosition)
            } else {
                storePreviousNoteValues()
            }
        } else {
            saveNote()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mViewModel!!.saveState(outState)
    }

    private fun storePreviousNoteValues() {
        val course = DataManager.getCourse(mViewModel!!.mOriginalNoteCourseId)
        mNote!!.course = course
        mNote!!.title = mViewModel!!.mOriginalNoteTitle
        mNote!!.text = mViewModel!!.mOriginalNoteText
    }

    private fun saveNote() {
        mNote!!.course = mSpinnerCourses!!.selectedItem as CourseInfo
        mNote!!.title = mTextNoteTitle!!.text.toString()
        mNote!!.text = mTextNoteText!!.text.toString()
    }

    private fun displayNote(spinnerCourses: Spinner, textNoteTitle: EditText, textNoteText: EditText) {
        val courses = DataManager.courses
        val courseIndex = courses.indexOf(mNote!!.course)
        spinnerCourses.setSelection(courseIndex)
        textNoteTitle.setText(mNote!!.title)
        textNoteText.setText(mNote!!.text)
    }

    private fun readDisplayStateValues() {
        val intent = intent
        val position = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET)
        mIsNewNote = position == POSITION_NOT_SET
        if (mIsNewNote) {
            createNewNote()
        } else {
            mNote = DataManager.notes[position]
        }
    }

    private fun createNewNote() {
        val dm = DataManager
        mNotePosition = dm.createNewNote()
        mNote = dm.notes[mNotePosition]
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_send_mail) {
            sendEmail()
            return true
        } else if (id == R.id.action_cancel) {
            mIsCancelling = true
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sendEmail() {
        val course = mSpinnerCourses!!.selectedItem as CourseInfo
        val subject = mTextNoteTitle!!.text.toString()
        val text = "Checkout what I learned in the Pluralsight course \"" +
                course.title + "\"\n" + mTextNoteText!!.text
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc2822"
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(intent)
    }

    companion object {
        const val NOTE_POSITION = "com.example.notekeeper.NOTE_POSITION"
        const val POSITION_NOT_SET = -1
    }
}