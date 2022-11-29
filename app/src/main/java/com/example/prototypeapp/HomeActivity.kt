package com.example.prototypeapp

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.prototypeapp.databinding.ActivityHomedashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomedashboardBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fstore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomedashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val profFragment = ProfFragment()

        val data = Bundle()
        var usernameText = "No Username"

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        val uid = user!!.uid
        fstore = FirebaseFirestore.getInstance()
        val docRef = fstore.collection("Users").document(uid)

        docRef.get().addOnSuccessListener { documentSnapshot ->
            if(!documentSnapshot.exists()){
                Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }
            usernameText = documentSnapshot.getString("userName").toString()
            data.putString("username", usernameText)

            profFragment.arguments = data

            openFragment(HomeFragment())

            binding.bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.homenav -> {
                        openFragment(HomeFragment())
                        return@setOnItemSelectedListener true
                    }
                    R.id.notifnav -> {
                        openFragment(NotificationsFragment())
                        return@setOnItemSelectedListener true
                    }
                    R.id.camnav ->{
                        openFragment(CameraFragment())
                        return@setOnItemSelectedListener true
                    }R.id.mailnav ->{
                    openFragment(MailFragment())
                    return@setOnItemSelectedListener true
                }
                    R.id.profnav ->{
                        openFragment(profFragment)
                        return@setOnItemSelectedListener true
                    }
                }
                return@setOnItemSelectedListener true
            }
        }

    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Log Out")
            .setMessage("Are you sure you want to Log Out?")
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, which -> finish() })
            .setNegativeButton("No", null)
            .show()
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().disallowAddToBackStack().replace(R.id.homeframelayout, fragment).commit()
    }
}