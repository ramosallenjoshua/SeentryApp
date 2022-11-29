package com.example.prototypeapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.BitmapCompat
import com.example.prototypeapp.databinding.ActivityCameraBinding
import com.example.prototypeapp.databinding.ActivityHomedashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.jar.Manifest

class CameraActivity : AppCompatActivity(){

    private lateinit var binding: ActivityCameraBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val faceScanButton = binding.faceScanButton
        val uploadAsClueButton = binding.uploadAsClueButton
        val takeAnotherPhotoButton = binding.takeAnotherPhotoButton
        val bitmap = intent.getParcelableExtra<Bitmap>("Bitmap")

        binding.photoTakenIMG.setImageBitmap(bitmap)

        faceScanButton.setOnClickListener{
            uploadImageforScan(bitmap!!)
        }


    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Discard")
            .setMessage("Do you wish to discard this image?")
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, which -> finish() })
            .setNegativeButton("No", null)
            .show()
    }

    private fun uploadImageforScan(bitmap: Bitmap){
        storage = Firebase.storage("gs://prototypeapp-606be.appspot.com/")
        mAuth = FirebaseAuth.getInstance()
        val storageRef = storage.reference
        val pathRef = storageRef.child("PendingUploads/${mAuth.currentUser!!.uid}-${System.currentTimeMillis()}"+".jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = pathRef.putBytes(data)
            .addOnFailureListener{
                Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener {
                Toast.makeText(this, "Upload Successful", Toast.LENGTH_SHORT).show()
                Log.d("SUCCESS", "DocumentSnapshot added with ID: ${mAuth.currentUser!!.uid}")
                finish()
            }
    }
}