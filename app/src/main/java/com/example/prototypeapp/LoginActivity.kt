package com.example.prototypeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.prototypeapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.loginButton2.setOnClickListener{
            val email = binding.loginEmailEditText.text.toString()
            val password = binding.loginPasswordEditText.text.toString()
            val adminemail = "admin"
            val adminpass = "admin"

            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Missing Fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                if (!it.isSuccessful){
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }

            //Temp Admin pass for Offline Viewing
            if(binding.loginEmailEditText.text.toString() == adminemail && binding.loginPasswordEditText.text.toString() == adminpass){
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }
    }

}