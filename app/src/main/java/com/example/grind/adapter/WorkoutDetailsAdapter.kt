package com.example.grind.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.grind.R
import com.example.grind.models.WorkoutDetailsClass

class WorkoutDetailsAdapter(private val list: List<WorkoutDetailsClass>) :
    RecyclerView.Adapter<WorkoutDetailsAdapter.DetailViewHolder>() {

    class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.workoutdetailTitle)
        val image: ImageView = itemView.findViewById(R.id.workoutdetailImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.workout_details_rv_item, parent, false)
        return DetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.title
        holder.image.setImageResource(item.imageResId)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val url = item.youtubeUrl

            if (url.isNotBlank()) {
                val youtubeIntent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                    setPackage("com.google.android.youtube") // Try to open in YouTube app
                }

                // Try YouTube app first, fallback to browser if not available
                if (youtubeIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(youtubeIntent)
                } else {
                    // Fallback to browser
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(browserIntent)
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size
}
