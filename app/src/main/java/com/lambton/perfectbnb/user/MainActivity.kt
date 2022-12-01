package com.lambton.perfectbnb.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationView
import com.lambton.perfectbnb.R
import com.lambton.perfectbnb.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.header_layout.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Adding Map Fragment on Activity
        val fragment = MapsFragment()
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
        txt_title.text="PerfectBnB"
    }
}