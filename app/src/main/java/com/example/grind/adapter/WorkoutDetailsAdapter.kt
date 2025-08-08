package com.example.grind.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.grind.R
import com.example.grind.models.WorkoutStep

class WorkoutStepAdapter(
    private val steps: MutableList<WorkoutStep>
) : RecyclerView.Adapter<WorkoutStepAdapter.StepViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_step, parent, false)
        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val step = steps[position]
        holder.bind(step)
    }

    override fun getItemCount(): Int = steps.size

    inner class StepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val etStepDesc: EditText = itemView.findViewById(R.id.etStepDesc)
        private val etStepLink: EditText = itemView.findViewById(R.id.etStepLink)

        fun bind(step: WorkoutStep) {
            etStepDesc.setText(step.stepDescription)
            etStepLink.setText(step.stepLink)

            // Remove previous listeners to avoid duplication
            etStepDesc.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    step.stepDescription = s.toString()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            etStepLink.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    step.stepLink = s.toString()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    fun addStep(step: WorkoutStep) {
        steps.add(step)
        notifyItemInserted(steps.size - 1)
    }

    fun getSteps(): List<WorkoutStep> = steps
}
