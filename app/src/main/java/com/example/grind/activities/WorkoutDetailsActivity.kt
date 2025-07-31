package com.example.grind.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grind.R
import com.example.grind.adapter.WorkoutDetailsAdapter
import com.example.grind.models.WorkoutCategoryClass
import com.example.grind.models.WorkoutDetailsClass

class WorkoutDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_details)

        val getData = intent.getParcelableExtra<WorkoutCategoryClass>("android")

        if (getData != null) {
            val detailDesc: TextView = findViewById(R.id.workoutdescription)
            val detailImage: ImageView = findViewById(R.id.workoutimg)
            val detailRecyclerView: RecyclerView = findViewById(R.id.workoutdetailsrv)

            detailDesc.text = getData.workoutdesc
            detailImage.setImageResource(getData.workoutcatimg)

            // ✅ Fix: Don't lowercase, match exactly with workoutinfo string
            val detailList = getDetailListForCategory(getData.workoutinfo)

            detailRecyclerView.layoutManager = LinearLayoutManager(this)
            detailRecyclerView.adapter = WorkoutDetailsAdapter(detailList)
        }
    }

    private fun getDetailListForCategory(category: String): List<WorkoutDetailsClass> {
        return when (category) {
            "FullBodyWorkout" -> listOf(
                WorkoutDetailsClass("Burpees", R.drawable.pic_1, "https://www.youtube.com/watch?v=TU8QYVW0gDU"),
                WorkoutDetailsClass("Mountain Climbers", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=nmwgirgXLYM"),
                WorkoutDetailsClass("Jump Squats", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=U4s4mEQ5VqU"),
                WorkoutDetailsClass("Push-Ups", R.drawable.pushup, "https://www.youtube.com/watch?v=_l3ySVKYVJ8"),
                WorkoutDetailsClass("Plank", R.drawable.plank, "https://www.youtube.com/watch?v=pSHjTRCQxIw")
            )

            "UpperBodyWorkout" -> listOf(
                WorkoutDetailsClass("Pull-Ups", R.drawable.pullup, "https://www.youtube.com/watch?v=eGo4IYlbE5g"),
                WorkoutDetailsClass("Push-Ups", R.drawable.pushup, "https://www.youtube.com/watch?v=_l3ySVKYVJ8"),
                WorkoutDetailsClass("Overhead Press", R.drawable.overhead, "https://www.youtube.com/watch?v=B-aVuyhvLHU"),
                WorkoutDetailsClass("Dumbbell Rows", R.drawable.dumbellrow, "https://www.youtube.com/watch?v=pYcpY20QaE8")
            )

            "Legworkout" -> listOf(
                WorkoutDetailsClass("Bodyweight Squats", R.drawable.pic_1, "https://www.youtube.com/watch?v=aclHkVaku9U"),
                WorkoutDetailsClass("Lunges", R.drawable.lunge_img, "https://www.youtube.com/watch?v=QOVaHwm-Q6U"),
                WorkoutDetailsClass("Calf Raises", R.drawable.calf, "https://www.youtube.com/watch?v=YMmgqO8Jo-k"),
                WorkoutDetailsClass("Glute Bridges", R.drawable.glutebridge, "https://www.youtube.com/watch?v=m2Zx-57cSok")
            )

            "Chestworkout" -> listOf(
                WorkoutDetailsClass("Incline Dumbbell Press", R.drawable.inclinedumbell, "https://www.youtube.com/watch?v=8iPEnn-ltC8"),
                WorkoutDetailsClass("Chest Dips", R.drawable.chestdip, "https://www.youtube.com/watch?v=2z8JmcrW-As"),
                WorkoutDetailsClass("Push-Ups", R.drawable.pushup, "https://www.youtube.com/watch?v=_l3ySVKYVJ8"),
                WorkoutDetailsClass("Cable Crossover", R.drawable.cablecrossover, "https://www.youtube.com/watch?v=taI4XduLpTk")
            )

            "BackWorkout" -> listOf(
                WorkoutDetailsClass("Deadlift", R.drawable.pic_1, "https://www.youtube.com/watch?v=op9kVnSso6Q"),
                WorkoutDetailsClass("Lat Pulldown", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=CAwf7n6Luuc"),
                WorkoutDetailsClass("Barbell Row", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=vT2GjY_Umpw"),
                WorkoutDetailsClass("Reverse Fly", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=EwRrLzN-uYM")
            )

            "ShoulderWorkout" -> listOf(
                WorkoutDetailsClass("Arnold Press", R.drawable.pic_1, "https://www.youtube.com/watch?v=vj2w851ZHRM"),
                WorkoutDetailsClass("Lateral Raises", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=3VcKaXpzqRo"),
                WorkoutDetailsClass("Front Raises", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=-t7fuZ0KhDA"),
                WorkoutDetailsClass("Face Pulls", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=rep-qVOkqgk")
            )

            "bicepWorkout" -> listOf(
                WorkoutDetailsClass("Barbell Curl", R.drawable.pic_1, "https://www.youtube.com/watch?v=kwG2ipFRgfo"),
                WorkoutDetailsClass("Hammer Curl", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=zC3nLlEvin4"),
                WorkoutDetailsClass("Concentration Curl", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=kwG2ipFRgfo")
            )

            "tricepworkout" -> listOf(
                WorkoutDetailsClass("Tricep Dips", R.drawable.pic_1, "https://www.youtube.com/watch?v=0326dy_-CzM"),
                WorkoutDetailsClass("Overhead Extension", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=_gsUck-7M74"),
                WorkoutDetailsClass("Tricep Pushdown", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=vB5OHsJ3EME")
            )

            "Abdomenworkout" -> listOf(
                WorkoutDetailsClass("Crunches", R.drawable.pic_1, "https://www.youtube.com/watch?v=Xyd_fa5zoEU"),
                WorkoutDetailsClass("Leg Raises", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=l4kQd9eWclE"),
                WorkoutDetailsClass("Plank", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=pSHjTRCQxIw"),
                WorkoutDetailsClass("Russian Twists", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=wkD8rjkodUI")
            )

            "HipWorkout" -> listOf(
                WorkoutDetailsClass("Hip Thrusts", R.drawable.pic_1, "https://www.youtube.com/watch?v=LM8XHLYJoYs"),
                WorkoutDetailsClass("Lateral Band Walks", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=2XUwYfLNBbU")
            )

            "WaistWorkout" -> listOf(
                WorkoutDetailsClass("Side Plank", R.drawable.pic_1, "https://www.youtube.com/watch?v=K2VljzCC16g"),
                WorkoutDetailsClass("Standing Side Crunch", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=V9CKj4g6ssA")
            )

            "ArmWorkout" -> listOf(
                WorkoutDetailsClass("Close-Grip Pushup", R.drawable.pic_1, "https://www.youtube.com/watch?v=4g5gkU_JJYg"),
                WorkoutDetailsClass("Bicep Curls", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=ykJmrZ5v0Oo"),
                WorkoutDetailsClass("Tricep Kickbacks", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=6SS6K3lAwZ8")
            )

            "AnkleWorkout" -> listOf(
                WorkoutDetailsClass("Heel Raises", R.drawable.pic_1, "https://www.youtube.com/watch?v=0_A7KsnxXNg"),
                WorkoutDetailsClass("Ankle Circles", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=j3AbRRIPrls")
            )

            else -> listOf(
                WorkoutDetailsClass("Burpees", R.drawable.pic_1, "https://www.youtube.com/watch?v=TU8QYVW0gDU"),
                WorkoutDetailsClass("Push Ups", R.drawable.pic_1_1, "https://www.youtube.com/watch?v=_l3ySVKYVJ8")
            )
        }
    }

}
