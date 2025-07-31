package com.example.grind.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grind.R
import com.example.grind.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    private val binding :ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()

        binding.SignUpButton.setOnClickListener {
            val email = binding.emailInputEditText.text.toString()
            val name = binding.NameInputEditText.text.toString()
            val password = binding.passwordInputEditText.text.toString()

            if(email.isEmpty() || name.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){task->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,LoginActivity::class.java))
                        finish()
                    }
                    else{
                        Toast.makeText(this, "Signin failed :${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
        binding.GotoLogin.setOnClickListener {
            intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}