package com.riphah.food.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.riphah.food.R
import com.riphah.food.ui.authntication.SignupFragment

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportFragmentManager.commit {
            add(R.id.Fragment_container_login, SignupFragment())
            addToBackStack(null)

        }
    }
}
