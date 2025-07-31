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
    private val workoutcategorydatalist: ArrayList<WorkoutCategoryClass>
) : RecyclerView.Adapter<WorkoutCategoryAdapterClass.ViewholderClass>() {
    var onItemClick: ((WorkoutCategoryClass) -> Unit)? = null

    class ViewholderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvImage: ImageView = itemView.findViewById(R.id.workcatimg)
        val rvduration: TextView = itemView.findViewById(R.id.duration)
        val rvkcal: TextView = itemView.findViewById(R.id.kcal)
        val rvcnt: TextView = itemView.findViewById(R.id.noexcercise)
        val rvinfo:TextView = itemView.findViewById(R.id.Workoutinf)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewholderClass {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.workout_category, parent, false)
        return ViewholderClass(view)
    }

    override fun getItemCount(): Int {
        return workoutcategorydatalist.size
    }

    override fun onBindViewHolder(holder: ViewholderClass, position: Int) {
        val item = workoutcategorydatalist[position]
        holder.rvImage.setImageResource(item.workoutcatimg) // Adjust based on your model
        holder.rvduration.text = item.duration.toString()
        holder.rvkcal.text = item.cal.toString()
        holder.rvcnt.text = item.excercisecnt.toString()
        holder.rvinfo.text =item.workoutinfo.toString()

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(item)
        }
    }
}
