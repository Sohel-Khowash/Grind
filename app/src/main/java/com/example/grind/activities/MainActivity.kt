package com.example.grind.activities

import android.os.Bundle
import android.os.WorkDuration
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grind.Fragments.DiscoverFragment
import com.example.grind.Fragments.HomeFragment
import com.example.grind.Fragments.PlanFragment
import com.example.grind.Fragments.ProfileFragment
import com.example.grind.R
import com.example.grind.adapter.WorkoutCategoryAdapterClass
import com.example.grind.models.WorkoutCategoryClass
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView=findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.bottom_home ->{
                    ReplaceFragment(HomeFragment())
                    true
                }
                R.id.bottom_discover->{
                    ReplaceFragment(DiscoverFragment())
                    true
                }
                R.id.bottom_plan->{
                    ReplaceFragment(PlanFragment())
                    true
                }
                R.id.bottom_profile->{
                    ReplaceFragment(ProfileFragment())
                    true
                }
                else->false
            }

        }
        ReplaceFragment(HomeFragment())


    }
    private fun ReplaceFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.main_frame_layout,fragment).commit()
    }

    fun setAppTheme(isDarkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        prefs.edit() { putBoolean("darkMode", isDarkMode) }
    }
}