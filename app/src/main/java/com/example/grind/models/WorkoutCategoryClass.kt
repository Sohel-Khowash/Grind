// models/WorkoutCategoryClass.kt
package com.example.grind.models

data class WorkoutCategoryClass(
    val workoutcatimg: Int,       // drawable resource ID
    val excercisecnt: Int,        // number of exercises
    val workoutinfo: String       // name or short info
)

