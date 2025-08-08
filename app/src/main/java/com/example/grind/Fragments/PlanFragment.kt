package com.example.grind.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.grind.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import android.content.res.Configuration
import android.graphics.Color

class PlanFragment : Fragment() {

    private lateinit var calorieInput: EditText
    private lateinit var saveCalorieBtn: Button
    private lateinit var calorieChart: LineChart

    private lateinit var weightInput: EditText
    private lateinit var saveWeightBtn: Button
    private lateinit var weightChart: LineChart

    private lateinit var heightInput: EditText
    private lateinit var saveHeightBtn: Button

    private lateinit var bmiGauge: ProgressBar
    private lateinit var bmiValueText: TextView
    private lateinit var bmiLabel: TextView

    private lateinit var database: DatabaseReference

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

    private var latestWeight: Float? = null
    private var latestHeight: Float? = null

    private var calorieListener: ValueEventListener? = null
    private var weightListener: ValueEventListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_plan, container, false)

        calorieInput = view.findViewById(R.id.calorieInput)
        saveCalorieBtn = view.findViewById(R.id.saveCalorieBtn)
        calorieChart = view.findViewById(R.id.calorieChart)

        weightInput = view.findViewById(R.id.weightInput)
        saveWeightBtn = view.findViewById(R.id.saveWeightBtn)
        weightChart = view.findViewById(R.id.weightChart)

        heightInput = view.findViewById(R.id.heightInput)
        saveHeightBtn = view.findViewById(R.id.saveHeightBtn)

        bmiGauge = view.findViewById(R.id.bmiGauge)
        bmiValueText = view.findViewById(R.id.bmiValueText)
        bmiLabel = view.findViewById(R.id.bmiLabel)

        database = FirebaseDatabase.getInstance().reference

        fetchLatestWeightAndHeight()

        saveCalorieBtn.setOnClickListener {
            val calories = calorieInput.text.toString().toIntOrNull()
            if (calories == null) {
                Toast.makeText(context, "Enter valid calories", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val today = dateFormat.format(Date())
            database.child("calories").child(today).setValue(calories)
                .addOnSuccessListener {
                    Toast.makeText(context, "Calories saved/updated for today", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to save calories", Toast.LENGTH_SHORT).show()
                }
        }

        saveWeightBtn.setOnClickListener {
            val weight = weightInput.text.toString().toFloatOrNull()
            if (weight == null) {
                Toast.makeText(context, "Enter valid weight", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val today = dateFormat.format(Date())
            database.child("weight").child(today).setValue(weight)
                .addOnSuccessListener {
                    Toast.makeText(context, "Weight saved/updated for today", Toast.LENGTH_SHORT).show()
                    latestWeight = weight
                    updateBMIDisplay()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to save weight", Toast.LENGTH_SHORT).show()
                }
        }

        saveHeightBtn.setOnClickListener {
            val height = heightInput.text.toString().toFloatOrNull()
            if (height == null || height <= 0f) {
                Toast.makeText(context, "Enter valid height in meters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            database.child("userHeight").setValue(height)
                .addOnSuccessListener {
                    Toast.makeText(context, "Height saved", Toast.LENGTH_SHORT).show()
                    latestHeight = height
                    updateBMIDisplay()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to save height", Toast.LENGTH_SHORT).show()
                }
        }

        loadCalorieChart()
        loadWeightChart()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nightModeFlags = requireContext().resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK

        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                calorieInput.setTextColor(Color.WHITE)
                weightInput.setTextColor(Color.WHITE)
                heightInput.setTextColor(Color.WHITE)
                bmiValueText.setTextColor(Color.WHITE)
                bmiLabel.setTextColor(Color.WHITE)
            }
            Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                calorieInput.setTextColor(Color.BLACK)
                weightInput.setTextColor(Color.BLACK)
                heightInput.setTextColor(Color.BLACK)
                bmiValueText.setTextColor(Color.BLACK)
                bmiLabel.setTextColor(Color.BLACK)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        calorieListener?.let { database.child("calories").removeEventListener(it) }
        weightListener?.let { database.child("weight").removeEventListener(it) }
    }

    private fun fetchLatestWeightAndHeight() {
        database.child("userHeight").get().addOnSuccessListener { heightSnapshot ->
            latestHeight = heightSnapshot.getValue(Float::class.java) ?: 1.75f

            database.child("weight").orderByKey().limitToLast(1)
                .get().addOnSuccessListener { weightSnapshot ->
                    if (weightSnapshot.exists()) {
                        val lastEntry = weightSnapshot.children.first()
                        latestWeight = lastEntry.getValue(Float::class.java)
                    }
                    updateBMIDisplay()
                }
        }.addOnFailureListener {
            if (isAdded) {
                Toast.makeText(requireContext(), "Failed to load height. Using default.", Toast.LENGTH_SHORT).show()
            }
            latestHeight = 1.75f
            updateBMIDisplay()
        }
    }

    private fun updateBMIDisplay() {
        if (!isAdded) return

        val weight = latestWeight
        val height = latestHeight

        if (weight == null || height == null || height <= 0f) {
            bmiValueText.text = "BMI: --"
            bmiLabel.text = "BMI Status: --"
            bmiGauge.progress = 0
            bmiGauge.progressTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
            return
        }

        val bmi = weight / (height * height)
        bmiValueText.text = String.format("BMI: %.1f", bmi)

        val progressValue = bmi.toInt().coerceIn(0, 40)
        bmiGauge.progress = progressValue

        when {
            bmi < 18.5 -> {
                bmiLabel.text = "BMI Status: Underweight"
                bmiGauge.progressTintList = ContextCompat.getColorStateList(requireContext(), R.color.blue)
            }
            bmi < 25 -> {
                bmiLabel.text = "BMI Status: Normal"
                bmiGauge.progressTintList = ContextCompat.getColorStateList(requireContext(), R.color.red)
            }
            bmi < 30 -> {
                bmiLabel.text = "BMI Status: Overweight"
                bmiGauge.progressTintList = ContextCompat.getColorStateList(requireContext(), R.color.black)
            }
            else -> {
                bmiLabel.text = "BMI Status: Obese"
                bmiGauge.progressTintList = ContextCompat.getColorStateList(requireContext(), R.color.red)
            }
        }
    }

    private fun loadCalorieChart() {
        calorieListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded || view == null) return

                val entries = mutableListOf<Entry>()
                val labels = mutableListOf<String>()
                var index = 0f

                val sortedChildren = snapshot.children.sortedBy { it.key }

                for (child in sortedChildren) {
                    val dateKey = child.key ?: continue
                    val value = child.getValue(Int::class.java) ?: continue
                    entries.add(Entry(index, value.toFloat()))

                    try {
                        val date = dateFormat.parse(dateKey)
                        labels.add(if (date != null) displayDateFormat.format(date) else dateKey)
                    } catch (e: Exception) {
                        labels.add(dateKey)
                    }

                    index += 1f
                }

                val ctx = context ?: return
                val dataSet = LineDataSet(entries, "Calories")
                dataSet.color = ContextCompat.getColor(ctx, R.color.blue)
                dataSet.setDrawCircles(true)
                dataSet.lineWidth = 2f

                calorieChart.data = LineData(dataSet)

                val xAxis = calorieChart.xAxis
                xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                xAxis.setDrawGridLines(false)

                calorieChart.axisRight.isEnabled = false
                calorieChart.description.isEnabled = false

                calorieChart.invalidate()
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        database.child("calories").addValueEventListener(calorieListener!!)
    }

    private fun loadWeightChart() {
        weightListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded || view == null) return

                val entries = mutableListOf<Entry>()
                val labels = mutableListOf<String>()
                var index = 0f

                val sortedChildren = snapshot.children.sortedBy { it.key }

                for (child in sortedChildren) {
                    val dateKey = child.key ?: continue
                    val value = child.getValue(Float::class.java) ?: continue
                    entries.add(Entry(index, value))

                    try {
                        val date = dateFormat.parse(dateKey)
                        labels.add(if (date != null) displayDateFormat.format(date) else dateKey)
                    } catch (e: Exception) {
                        labels.add(dateKey)
                    }

                    index += 1f
                }

                val ctx = context ?: return
                val dataSet = LineDataSet(entries, "Weight")
                dataSet.color = ContextCompat.getColor(ctx, R.color.red)
                dataSet.setDrawCircles(true)
                dataSet.lineWidth = 2f

                weightChart.data = LineData(dataSet)

                val xAxis = weightChart.xAxis
                xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                xAxis.setDrawGridLines(false)

                weightChart.axisRight.isEnabled = false
                weightChart.description.isEnabled = false

                weightChart.invalidate()
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        database.child("weight").addValueEventListener(weightListener!!)
    }
}
