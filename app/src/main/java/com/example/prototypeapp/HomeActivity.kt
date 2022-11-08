package com.example.prototypeapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.prototypeapp.databinding.ActivityHomedashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


class HomeActivity : AppCompatActivity() {

    companion object{
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }

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
                        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
                            return@setOnItemSelectedListener  true
                        }
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(intent, CAMERA_REQUEST_CODE)
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
        val intent = Intent(this, LogoutActivity::class.java)
        startActivity(intent)
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().disallowAddToBackStack().replace(R.id.homeframelayout, fragment).commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode != CAMERA_PERMISSION_CODE){
            return
        }
        if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Access not granted", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != Activity.RESULT_OK){
            return
        }
        if(requestCode != CAMERA_REQUEST_CODE){
            return
        }
        val thumbnail: Bitmap = data!!.extras!!.get("data") as Bitmap

        val intent = Intent(this,CameraActivity::class.java)
        startActivity(intent)
    }

}