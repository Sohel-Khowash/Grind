package com.example.grind.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grind.R
import com.example.grind.activities.WorkoutDetailsActivity
import com.example.grind.adapter.WorkoutCategoryAdapterClass
import com.example.grind.models.WorkoutCategoryClass
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<WorkoutCategoryClass>
    private lateinit var originalList: ArrayList<WorkoutCategoryClass>
    private lateinit var imageList: Array<Int>
    private lateinit var duration: Array<Int>
    private lateinit var cal: Array<Int>
    private lateinit var workoutdesc: Array<String>
    private lateinit var workoutinfo: Array<String>
    private lateinit var noexercise: Array<Int>
    private lateinit var searchView: SearchView
    private lateinit var adapter: WorkoutCategoryAdapterClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageList = arrayOf(
            R.drawable.pic_1,
            R.drawable.pic_1_1,
            R.drawable.pic_1_2,
            R.drawable.pic_1_3,
            R.drawable.pic_2,
            R.drawable.pic_2_1,
            R.drawable.pic_2_2,
            R.drawable.pic_2_3,
            R.drawable.pic_2_4,
            R.drawable.pic_3,
            R.drawable.pic_3_1,
            R.drawable.pic_3_2,
            R.drawable.pic_3_3,
            R.drawable.pic_3_4
        )
        duration = arrayOf(65, 80, 100, 70, 80, 90, 50, 60, 10, 12, 13, 14, 15, 18)
        cal = arrayOf(500, 800, 2000, 800, 900, 600, 300, 822, 145, 624, 87, 96, 75, 788)
        noexercise = arrayOf(65, 80, 100, 70, 80, 90, 50, 60, 10, 12, 13, 14, 15, 18)
        workoutinfo = arrayOf(
            "FullBodyWorkout", "UpperBodyWorkout", "LowerBodyWorkout", "Chestworkout",
            "Legworkout", "BackWorkout", "HipWorkout", "WaistWorkout", "bicepWorkout",
            "tricepworkout", "Abdomenworkout", "ShoulderWorkout", "ArmWorkout", "AnkleWorkout"
        )

        dataList = arrayListOf()
        originalList = arrayListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize workoutdesc here since getString() requires attached context
        workoutdesc = arrayOf(
            getString(R.string.desc_full_body_workout),
            getString(R.string.desc_upper_body_workout),
            getString(R.string.desc_lower_body_workout),
            getString(R.string.desc_chest_workout),
            getString(R.string.desc_leg_workout),
            getString(R.string.desc_back_workout),
            getString(R.string.desc_hip_workout),
            getString(R.string.desc_waist_workout),
            getString(R.string.desc_bicep_workout),
            getString(R.string.desc_tricep_workout),
            getString(R.string.desc_abdomen_workout),
            getString(R.string.desc_shoulder_workout),
            getString(R.string.desc_arm_workout),
            getString(R.string.desc_ankle_workout)
        )

        recyclerView = view.findViewById(R.id.workoutcategoryrv)
        searchView = view.findViewById(R.id.home_search)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        getData()

        adapter = WorkoutCategoryAdapterClass(dataList)
        recyclerView.adapter = adapter

        adapter.onItemClick = {
            val intent = Intent(requireContext(), WorkoutDetailsActivity::class.java)
            intent.putExtra("android", it)
            startActivity(intent)
        }

        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText = newText?.lowercase(Locale.getDefault()).orEmpty()
                dataList.clear()

                if (searchText.isNotEmpty()) {
                    for (item in originalList) {
                        if (item.workoutinfo.lowercase(Locale.getDefault()).contains(searchText)) {
                            dataList.add(item)
                        }
                    }
                } else {
                    dataList.addAll(originalList)
                }

                adapter.notifyDataSetChanged()
                return false
            }
        })

        return view
    }

    private fun getData() {
        for (i in imageList.indices) {
            val dataclass = WorkoutCategoryClass(
                imageList[i],
                duration[i],
                noexercise[i],
                cal[i],
                workoutinfo[i],
                workoutdesc[i]
            )
            dataList.add(dataclass)
        }
        originalList.addAll(dataList)
    }
}
