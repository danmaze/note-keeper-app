package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NoteListActivity : AppCompatActivity() {

    private var mAdapterNotes: ArrayAdapter<NoteInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            startActivity(
                Intent(
                    this@NoteListActivity,
                    NoteActivity::class.java
                )
            )
        }

        initializeDisplayContent()
    }

    override fun onResume() {
        super.onResume()
        mAdapterNotes!!.notifyDataSetChanged()
    }

    private fun initializeDisplayContent() {
        val listNotes = findViewById<ListView>(R.id.list_notes)
        val notes = DataManager.notes
        mAdapterNotes = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, notes
        )
        listNotes.adapter = mAdapterNotes
        listNotes.setOnItemClickListener { adapterView, view, position, l ->
            val intent = Intent(this@NoteListActivity, NoteActivity::class.java)
            intent.putExtra(NoteActivity.NOTE_POSITION, position)
            startActivity(intent)
        }

    }

}