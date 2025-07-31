package com.example.grind.models

import android.os.Parcel
import android.os.Parcelable

data class WorkoutCategoryClass(
    var workoutcatimg: Int,
    var duration: Int,
    var excercisecnt: Int,
    var cal: Int,
    var workoutinfo: String,
    var workoutdesc: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(workoutcatimg)
        dest.writeInt(duration)
        dest.writeInt(excercisecnt)
        dest.writeInt(cal)
        dest.writeString(workoutinfo)
        dest.writeString(workoutdesc)
    }

    companion object CREATOR : Parcelable.Creator<WorkoutCategoryClass> {
        override fun createFromParcel(parcel: Parcel): WorkoutCategoryClass {
            return WorkoutCategoryClass(parcel)
        }

        override fun newArray(size: Int): Array<WorkoutCategoryClass?> {
            return arrayOfNulls(size)
        }
    }
}
