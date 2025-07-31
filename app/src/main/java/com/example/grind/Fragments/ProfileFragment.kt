package com.example.grind.Fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.grind.R
import com.example.grind.activities.LoginActivity // <-- Make sure this is your login activity
import com.example.grind.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {

    private lateinit var profilePic: CircleImageView
    private lateinit var changePicBtn: Button
    private lateinit var themeToggleBtn: Button
    private lateinit var logoutBtn: Button

    private val PICK_IMAGE_REQUEST = 1
    private var isDarkMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profilePic = view.findViewById(R.id.userprofilepic)
        changePicBtn = view.findViewById(R.id.changeProfilePicBtn)
        themeToggleBtn = view.findViewById(R.id.themeToggleBtn)
        logoutBtn = view.findViewById(R.id.logoutBtn)

        changePicBtn.setOnClickListener { pickImage() }
        themeToggleBtn.setOnClickListener { toggleTheme() }
        logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()

            // Redirect to LoginActivity and clear back stack
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            // Finish current activity hosting this fragment
            requireActivity().finish()
        }

        loadProfilePicture()
        loadThemePreference()

        return view
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null) {
                profilePic.setImageURI(imageUri)
                saveImageToDatabase(imageUri)
            }
        }
    }

    private fun saveImageToDatabase(imageUri: Uri) {
        val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val imageBytes = baos.toByteArray()
        val imageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(uid)
                .child("profilePic")
                .setValue(imageBase64)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Profile picture updated", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun loadProfilePicture() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(uid)
            .child("profilePic")
            .get()
            .addOnSuccessListener { snapshot ->
                val base64 = snapshot.getValue(String::class.java)
                if (!base64.isNullOrEmpty()) {
                    val imageBytes = Base64.decode(base64, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    profilePic.setImageBitmap(bitmap)
                }
            }
    }

    private fun toggleTheme() {
        isDarkMode = !isDarkMode
        (activity as? MainActivity)?.setAppTheme(isDarkMode)
        themeToggleBtn.text = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode"
    }

    private fun loadThemePreference() {
        val prefs = requireActivity().getSharedPreferences("settings", Activity.MODE_PRIVATE)
        isDarkMode = prefs.getBoolean("darkMode", false)
        themeToggleBtn.text = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode"
    }
}
