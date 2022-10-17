package com.example.prototypeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.prototypeapp.databinding.ActivityHomedashboardBinding
import com.example.prototypeapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomedashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomedashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                    openFragment(ProfFragment())
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, LogoutActivity::class.java)
        startActivity(intent)
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().disallowAddToBackStack().replace(R.id.homeframelayout, fragment).commit()
    }
}