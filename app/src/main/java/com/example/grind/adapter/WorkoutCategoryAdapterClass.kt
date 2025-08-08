package com.example.grind.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.grind.R
import com.example.grind.models.WorkoutCategoryClass

class WorkoutCategoryAdapterClass(
    private val workoutCategoryList: ArrayList<WorkoutCategoryClass>
) : RecyclerView.Adapter<WorkoutCategoryAdapterClass.ViewHolder>() {

    // Lambda for handling item click events
    var onItemClick: ((WorkoutCategoryClass) -> Unit)? = null

    // ViewHolder class to hold views for each item
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryImage: ImageView = itemView.findViewById(R.id.workcatimg)
        val exerciseCount: TextView = itemView.findViewById(R.id.noexcercise)
        val workoutInfo: TextView = itemView.findViewById(R.id.Workoutinf)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.workout_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = workoutCategoryList[position]

        // Bind data to views
        holder.categoryImage.setImageResource(item.workoutcatimg)
        holder.exerciseCount.text = "${item.excercisecnt} exercises"
        holder.workoutInfo.text = item.workoutinfo

        // Handle item click
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int = workoutCategoryList.size
}
