package com.example.prototypeapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.prototypeapp.databinding.FragmentProfBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.RuntimeException


class ProfFragment : Fragment() {

    private lateinit var binding: FragmentProfBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfBinding.inflate(inflater, container, false)

        val username = arguments?.getString("username")
        val usernameText = binding.root.findViewById<TextView>(R.id.usernameText)
        usernameText.text = username

        return binding.root
    }
}