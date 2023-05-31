package com.riphah.food.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.riphah.food.R
import com.riphah.food.ui.authntication.LoginFragment
import com.riphah.food.ui.authntication.SignupFragment
import com.riphah.food.utils.FirebaseUtils.firebaseUser


class LoginActivity : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        firebaseUser?.let {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportFragmentManager.commit {
            add(R.id.Fragment_container_login, LoginFragment())
            addToBackStack(null)

        }
    }
}
