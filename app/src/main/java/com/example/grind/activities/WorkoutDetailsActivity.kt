package com.example.grind.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.grind.R
import com.example.grind.adapter.WorkoutStepThumbnailAdapter
import com.example.grind.databinding.ActivityWorkoutDetailsBinding
import com.example.grind.models.WorkoutDetailsClass

class WorkoutDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutDetailsBinding
    private lateinit var workoutStepAdapter: WorkoutStepThumbnailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get workout data from intent
        val workout = intent.getParcelableExtra<WorkoutDetailsClass>("workoutData")
        val workoutId = intent.getStringExtra("workoutId") ?: ""
        val workoutInfo = intent.getStringExtra("workoutName") ?: "" // ✅ from WorkoutCategoryClass

        if (workoutId.isEmpty()) {
            Toast.makeText(this, "Workout ID is empty ", Toast.LENGTH_SHORT).show()
        }
         if(workoutInfo.isEmpty()){
             Toast.makeText(this, "Workout Info is empty ", Toast.LENGTH_SHORT).show()
         }

        workout?.let {
            // Show workout details
            binding.workoutName.text = workoutInfo // ✅ now using workoutinfo instead of it.name
            binding.workoutDescription.text = it.description

            if (it.imageUrl.isNotEmpty()) {
                Glide.with(this)
                    .load(it.imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.workoutImage)
            } else {
                binding.workoutImage.setImageResource(R.drawable.placeholder)
            }

            // Set adapter for steps
            workoutStepAdapter = WorkoutStepThumbnailAdapter(it.steps, workoutId) { wid, stepIndex ->
                val intent = Intent(this, TrailerActivity::class.java)
                intent.putExtra("workoutId", wid) // pass ID
                intent.putExtra("workoutName", workoutInfo) // ✅ pass category workoutinfo
                intent.putExtra("stepIndex", stepIndex)
                intent.putExtra("videoUrl", it.steps[stepIndex].stepLink)
                startActivity(intent)
            }

            binding.stepsRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.stepsRecyclerView.adapter = workoutStepAdapter
        }
    }
}
