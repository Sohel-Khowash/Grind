package com.example.grind.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.grind.activities.MainActivity
import com.example.grind.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
//    private val binding:ActivityLoginBinding by lazy {
//        ActivityLoginBinding.inflate(layoutInflater)
//    }
    private  val binding:ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
}
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onStart() {
        super.onStart()
        val currentuser : FirebaseUser? = auth.currentUser
        if(currentuser != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()

        binding.LoginButton.setOnClickListener {
            val email = binding.emailInputEditText.text.toString()
            val password = binding.passwordInputEditText.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "fill all the details", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task->
                        if(task.isSuccessful){
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(this, "signin failed :${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        binding.GotoSignUp.setOnClickListener {
            intent=Intent(this,SignupActivity::class.java)
            startActivity(intent)
            finish()
        }



    }
}