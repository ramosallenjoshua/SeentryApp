package com.example.prototypeapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.prototypeapp.databinding.FragmentCameraBinding
import com.example.prototypeapp.databinding.FragmentProfBinding

class CameraFragment : Fragment() {

    companion object{
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }

    private lateinit var binding: FragmentCameraBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        val takePhoto = binding.takePhotoIMG
        val getFromStorage = binding.getFromStorageIMG

        takePhoto.setOnClickListener{
            Toast.makeText(activity, "Opening Camera", Toast.LENGTH_SHORT).show()
            if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
                //ActivityCompat.requestPermissions(HomeActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
                return@setOnClickListener
            }
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }

        getFromStorage.setOnClickListener{

        }

        return binding.root
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
            Toast.makeText(activity, "Access not granted", Toast.LENGTH_SHORT).show()
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

        val intent = Intent(activity,CameraActivity::class.java)
        intent.putExtra("Bitmap", thumbnail)
        startActivity(intent)
    }

}