package com.example.grind.Fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.grind.R
import com.example.grind.activities.DiscoverDetailsActivity
import com.example.grind.adapter.DiscoverAdapter
import com.example.grind.databinding.FragmentDiscoverBinding
import com.example.grind.models.DiscoverItemClass

class DiscoverFragment : Fragment() {

    private lateinit var binding: FragmentDiscoverBinding
    private lateinit var trainerArrayList: ArrayList<DiscoverItemClass>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.insetsController?.hide(WindowInsets.Type.statusBars())

        trainerArrayList = arrayListOf(
            DiscoverItemClass(R.drawable.trainer2, "Aarav Singh", "Ready to push limits!", "6:45 AM", "9876543210", "India"),
            DiscoverItemClass(R.drawable.trainer3, "Emma Johnson", "Let’s get moving!", "8:30 AM", "1234567890", "USA"),
            DiscoverItemClass(R.drawable.trainer4, "Liam Chen", "Mind over matter.", "5:00 PM", "9988776655", "China"),
            DiscoverItemClass(R.drawable.trainer5, "Sophia Müller", "No pain, no gain!", "7:15 PM", "5566778899", "Germany"),
            DiscoverItemClass(R.drawable.trainer6, "Carlos Rivera", "Stronger every day.", "4:00 PM", "4455667788", "Mexico"),
            DiscoverItemClass(R.drawable.trainer7, "Aya Nakamura", "Stay consistent!", "9:00 AM", "3344556677", "Japan"),
            DiscoverItemClass(R.drawable.trainer2, "Noah Kim", "Fuel your hustle!", "2:30 PM", "1122334455", "South Korea"),
            DiscoverItemClass(R.drawable.trainer3, "Chloe Dubois", "Let’s crush it!", "6:00 PM", "2233445566", "France")
        )

        val adapter = DiscoverAdapter(requireContext(), trainerArrayList)
        binding.discoverListview.adapter = adapter

        binding.discoverListview.setOnItemClickListener { _, _, position, _ ->
            val trainer = trainerArrayList[position]
            val intent = Intent(requireContext(), DiscoverDetailsActivity::class.java).apply {
                putExtra("name", trainer.name)
                putExtra("country", trainer.country)
                putExtra("image", trainer.image)
                putExtra("phone", trainer.phoneno)
            }
            startActivity(intent)
        }
    }
}
