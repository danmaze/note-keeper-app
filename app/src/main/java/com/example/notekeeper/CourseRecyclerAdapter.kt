package com.example.notekeeper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class CourseRecyclerAdapter(private val mContext: Context, private val mCourses: List<CourseInfo>) :
    RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder>() {
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = mLayoutInflater.inflate(R.layout.item_course_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = mCourses[position]
        holder.mTextCourse.text = course.title
        holder.mCurrentPosition = position
    }

    override fun getItemCount(): Int {
        return mCourses.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val mTextCourse: TextView = itemView.findViewById(R.id.text_course)
        var mCurrentPosition: Int = 0

        init {
            itemView.setOnClickListener { v ->
                Snackbar.make(
                    v, mCourses[mCurrentPosition].title,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}