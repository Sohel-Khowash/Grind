package com.example.grind.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutDetailsClass(
    val title: String,
    val imageResId: Int,
    val youtubeUrl: String = ""
) : Parcelable
