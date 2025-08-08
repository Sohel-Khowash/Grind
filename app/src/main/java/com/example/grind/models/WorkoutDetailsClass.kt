package com.example.grind.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutDetailsClass(
    var name: String = "",                     // Name of the workout (mandatory)
    var imageUrl: String = "",                 // Optional image URL
    var description: String = "",              // Description of the workout
    var steps: List<WorkoutStep> = emptyList() // List of steps (each with video, note, description)
) : Parcelable
