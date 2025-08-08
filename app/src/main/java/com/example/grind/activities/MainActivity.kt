package com.example.grind.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.grind.Fragments.DiscoverFragment
import com.example.grind.Fragments.HomeFragment
import com.example.grind.Fragments.PlanFragment
import com.example.grind.Fragments.ProfileFragment
import com.example.grind.R
import com.example.yourapp.fragments.StepCounterFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saveTestImagePathToFirebase()

        bottomNavigation = findViewById(R.id.bottom_navigation)

        // Load default fragment
        replaceFragment(HomeFragment())

        bottomNavigation.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment? = when (item.itemId) {
                R.id.bottom_home -> HomeFragment()
                R.id.bottom_discover -> StepCounterFragment()
                R.id.bottom_plan -> PlanFragment()
                R.id.bottom_profile -> ProfileFragment()
                else -> null
            }

            selectedFragment?.let {
                replaceFragment(it)
                true
            } ?: false
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame_layout, fragment)
            .commit()
    }

    fun setAppTheme(isDarkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        prefs.edit().putBoolean("darkMode", isDarkMode).apply()
    }

    private fun saveTestImagePathToFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        } else {
            Toast.makeText(this, "User logged in: $userId", Toast.LENGTH_SHORT).show()
        }

        val dummyImageUrl =
            "https://firebasestorage.googleapis.com/v0/b/fir-dummy.appspot.com/o/sample.jpg?alt=media"
        val dbRef = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(userId)
            .child("profilePic")

        dbRef.setValue(dummyImageUrl)
            .addOnSuccessListener {
                Toast.makeText(this, "Dummy image URL stored", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Failed: ${error.message}", Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            }
    }
}
