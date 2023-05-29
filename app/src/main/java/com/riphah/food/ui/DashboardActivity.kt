package com.riphah.food.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.riphah.food.R

class DashboardActivity : AppCompatActivity() {
    lateinit var bottomNavigationBar:BottomNavigationView;
    lateinit var navController:NavController;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        bottomNavigationBar=findViewById(R.id.bottom_navigation)
        navController= findNavController(R.id.fragNavHost)
        setupViews()

    }
    private fun setupViews(){
        bottomNavigationBar.setupWithNavController(navController)
    }

}