package com.example.grind.activities

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.grind.R
import com.example.grind.databinding.ActivityDiscoverDetailsBinding
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi

class DiscoverDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiscoverDetailsBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // HIDE STATUS BAR (API 30+)
        window.insetsController?.hide(WindowInsets.Type.statusBars())

        // Set view after configuring fullscreen
        binding = ActivityDiscoverDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get and set data
        val name = intent.getStringExtra("name")
        val phone = intent.getStringExtra("phone")
        val country = intent.getStringExtra("country")
        val imageid = intent.getIntExtra("image", R.drawable.trainer2)

        binding.countryName.text = country
        binding.trainerPhoneNo.text = phone
        binding.trainername.text = name
        binding.trainerprofilepic.setImageResource(imageid)
    }
}
