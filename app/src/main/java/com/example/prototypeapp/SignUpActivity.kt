package com.example.prototypeapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.prototypeapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.signUpButton2.setOnClickListener{
            val username = binding.usernameEditText.text.toString()
            val mobilenumber = binding.mobileNumberEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passEditText.text.toString()
            val cpassword = binding.cpassEditText.text.toString()
            val passReg = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$"
            val regPattern: Pattern = Pattern.compile(passReg)
            val patternMatcher: Matcher = regPattern.matcher(password)

            if(username.isEmpty() || mobilenumber.isEmpty() || email.isEmpty() || password.isEmpty() || cpassword.isEmpty()){
                Toast.makeText(this, "There is at least one missing field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //Check Password
            if(password.length < 8 || password.length > 16){
                Toast.makeText(this, "Password must be 8-16 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //Check if mobilenumber.length != 11
            if(mobilenumber.length != 11){
                Toast. makeText(this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!patternMatcher.matches()){
                Toast.makeText(this, "Password must contain at least one uppercase letter, one lowercase letter, and one number",
                Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (password != cpassword){
                Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                if(it.isSuccessful){
                    val fstore = Firebase.firestore
                    val userID = firebaseAuth.currentUser?.uid.toString()
                    val user = hashMapOf(
                        "userName" to username,
                        "firstName" to "",
                        "lastName" to "",
                        "mobileNumber" to mobilenumber,
                        "email" to email
                    )

                    fstore.collection("Users")
                        .document(userID)
                        .set(user)
                        .addOnSuccessListener {
                            Log.d("SUCCESS", "DocumentSnapshot added with ID: $userID")
                        }
                        .addOnFailureListener { e ->
                            Log.w("FAILED", "Error adding document: ", e)
                        }

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "SUCCESS", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}