package com.example.grind.Fragments

import com.example.grind.activities.WorkoutDetailsActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grind.R
import com.example.grind.activities.AddWorkoutActivity
import com.example.grind.adapter.WorkoutCategoryAdapterClass
import com.example.grind.models.WorkoutCategoryClass
import com.example.grind.models.WorkoutDetailsClass
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WorkoutCategoryAdapterClass
    private lateinit var searchView: SearchView
    private lateinit var dataList: ArrayList<WorkoutCategoryClass>
    private lateinit var originalList: ArrayList<WorkoutCategoryClass>

    private val workoutMap: MutableMap<String, WorkoutDetailsClass> = mutableMapOf()
    private val workoutIdMap: MutableMap<String, String> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataList = arrayListOf()
        originalList = arrayListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val imageView = view.findViewById<ImageView>(R.id.imageprofile)
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val userRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(userId)
                .child("profilePic")

            userRef.get().addOnSuccessListener { snapshot ->
                if (!isAdded) return@addOnSuccessListener
                val profilePicUrl = snapshot.getValue(String::class.java)
                if (!profilePicUrl.isNullOrEmpty()) {
                    Glide.with(requireContext())
                        .load(profilePicUrl)
                        .placeholder(R.drawable.placeholder)
                        .into(imageView)
                } else {
                    Log.w("HomeFragment", "No profile picture found.")
                }
            }.addOnFailureListener {
                if (!isAdded) return@addOnFailureListener
                Log.e("HomeFragment", "Failed to fetch profile picture: ${it.message}")
            }
        }

        recyclerView = view.findViewById(R.id.workoutcategoryrv)
        searchView = view.findViewById(R.id.home_search)
        val fab: FloatingActionButton = view.findViewById(R.id.fabAddWorkout)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        adapter = WorkoutCategoryAdapterClass(dataList)
        recyclerView.adapter = adapter

        adapter.onItemClick = onItemClick@{ selectedCategory ->
            if (!isAdded) return@onItemClick
            val categoryName = selectedCategory.workoutinfo
            val workout = workoutMap[categoryName]
            val workoutId = workoutIdMap[categoryName] ?: ""

            if (workout != null && workoutId.isNotEmpty()) {
                val intent = Intent(requireContext(), WorkoutDetailsActivity::class.java)
                intent.putExtra("workoutData", workout)
                intent.putExtra("workoutId", workoutId)
                intent.putExtra("workoutName", categoryName)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Workout details not found.", Toast.LENGTH_SHORT).show()
            }
        }

        fab.setOnClickListener {
            if (!isAdded) return@setOnClickListener
            startActivity(Intent(requireContext(), AddWorkoutActivity::class.java))
        }

        setupSearch()
        return view
    }

    override fun onResume() {
        super.onResume()
        loadWorkoutCategories()
    }

    private fun setupSearch() {
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
    }

    private fun loadWorkoutCategories() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val dbRef = FirebaseDatabase.getInstance().getReference("user_workouts").child(userId)

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded) return
                dataList.clear()
                originalList.clear()
                workoutMap.clear()
                workoutIdMap.clear()

                if (!snapshot.exists()) {
                    Toast.makeText(requireContext(), "No workouts found.", Toast.LENGTH_SHORT).show()
                    adapter.notifyDataSetChanged()
                    return
                }

                for (categorySnapshot in snapshot.children) {
                    val categoryName = categorySnapshot.key ?: continue

                    val validWorkouts = mutableListOf<Pair<String, WorkoutDetailsClass>>()
                    for (workoutSnapshot in categorySnapshot.children) {
                        val workout = workoutSnapshot.getValue(WorkoutDetailsClass::class.java)
                        val workoutId = workoutSnapshot.key
                        if (workout != null && workoutId != null) {
                            validWorkouts.add(workoutId to workout)
                        }
                    }

                    if (validWorkouts.isNotEmpty()) {
                        val (workoutId, workout) = validWorkouts.first()

                        workoutMap[categoryName] = workout
                        workoutIdMap[categoryName] = workoutId

                        val workoutCategory = WorkoutCategoryClass(
                            workoutcatimg = R.drawable.placeholder,
                            excercisecnt = validWorkouts.size,
                            workoutinfo = categoryName
                        )
                        dataList.add(workoutCategory)
                    }
                }

                originalList.addAll(dataList)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                if (!isAdded) return
                Toast.makeText(requireContext(), "Failed to load workouts: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e("HomeFragment", "Database error: ${error.details}")
            }
        })
    }
}
