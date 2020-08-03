package com.example.notekeeper

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteRecyclerAdapter(private val mContext: Context, private val mNotes: List<NoteInfo>) :
    RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>() {
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = mLayoutInflater.inflate(R.layout.item_note_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = mNotes[position]
        holder.mTextCourse.text = note.course!!.title
        holder.mTextTitle.text = note.title
        holder.mCurrentPosition = position
    }

    override fun getItemCount(): Int {
        return mNotes.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val mTextCourse: TextView = itemView.findViewById(R.id.text_course)
        val mTextTitle: TextView = itemView.findViewById(R.id.text_title)
        var mCurrentPosition: Int = 0

        init {
            itemView.setOnClickListener {
                val intent = Intent(mContext, NoteActivity::class.java)
                intent.putExtra(NoteActivity.NOTE_POSITION, mCurrentPosition)
                mContext.startActivity(intent)
            }
        }
    }
}