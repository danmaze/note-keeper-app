package com.example.notekeeper

import android.os.Parcel
import android.os.Parcelable

class NoteInfo : Parcelable {
    var course: CourseInfo? = null
    var title: String? = null
    var text: String? = null

    private val compareKey: String
        get() = course!!.courseId + "|" + title + "|" + text

    constructor(course: CourseInfo?, title: String?, text: String?) {
        this.course = course
        this.title = title
        this.text = text
    }

    private constructor(parcel: Parcel) {
        course = parcel.readParcelable(CourseInfo::class.java.classLoader)
        title = parcel.readString()
        text = parcel.readString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as NoteInfo?
        return compareKey == that!!.compareKey
    }

    override fun hashCode(): Int {
        return compareKey.hashCode()
    }

    override fun toString(): String {
        return compareKey
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeParcelable(course, 0)
        parcel.writeString(title)
        parcel.writeString(text)
    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<NoteInfo> = object : Parcelable.Creator<NoteInfo> {
            override fun createFromParcel(parcel: Parcel): NoteInfo {
                return NoteInfo(parcel)
            }

            override fun newArray(size: Int): Array<NoteInfo?> {
                return arrayOfNulls(size)
            }
        }
    }
}