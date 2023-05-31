package com.riphah.food.ui.authntication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import com.google.firebase.auth.FirebaseAuth
import com.riphah.food.R
import com.riphah.food.databinding.FragmentLoginBinding
import com.riphah.food.model.User
import com.riphah.food.showToast
import com.riphah.food.ui.DashboardActivity
import com.riphah.food.ui.LoginActivity

class LoginFragment : Fragment() {

    lateinit var binding:FragmentLoginBinding
    private lateinit var firebaseAuth:FirebaseAuth

    override fun onStart() {
        super.onStart()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth= FirebaseAuth.getInstance()
        setUpListeners()

    }

    private fun setUpListeners() {
        binding.textViewToSignUp.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.Fragment_container_login,SignupFragment())
                addToBackStack(null)
            }
        }
        binding.buttonLogin.setOnClickListener {
            val email=binding.UserEmailEditTextSignIn.text.toString().trim()
            val password=binding.UserPasswordEditTextSignIn.text.toString().trim()

            // Define regular expressions for email and password validation
            val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            val passwordPattern = "^(?=.*[0-9])(?=.*[!@#$%^&*])(?=\\S+$).{8,}$"

            // Check if email is empty or invalid
            if(email.isEmpty()){
                binding.UserEmailLayoutSignIn.error="Please enter email"
                binding.UserEmailLayoutSignIn.isErrorEnabled = true
            }else if(!email.matches(emailPattern.toRegex())){
                binding.UserEmailLayoutSignIn.error="Please enter a valid email"
                binding.UserEmailLayoutSignIn.isErrorEnabled = true
            }else{
                binding.UserEmailLayoutSignIn.error=null
                binding.UserEmailLayoutSignIn.isErrorEnabled = false
            }

            // Check if password is empty or invalid
            if(password.isEmpty()){
                binding.UserPasswordLayoutSignIn.error="Please enter password"
                binding.UserPasswordLayoutSignIn.isErrorEnabled = true
            }else if(!password.matches(passwordPattern.toRegex())){
                binding.UserPasswordLayoutSignIn.error="Please enter a strong password"
                binding.UserPasswordLayoutSignIn.isErrorEnabled = true
            }else{
                binding.UserPasswordLayoutSignIn.error=null
                binding.UserPasswordLayoutSignIn.isErrorEnabled = false
            }

            // Proceed to login only if both email and password are valid
            if(email.matches(emailPattern.toRegex()) && password.matches(passwordPattern.toRegex())){
                val user= User(null,email,password)
                loginUser(user)
            }
        }
    }

    private fun loginUser(user: User){
        firebaseAuth.signInWithEmailAndPassword(user.email,user.password).addOnCompleteListener {
            if (it.isSuccessful){
                val it= Intent(activity,DashboardActivity::class.java)
                startActivity(it)

            }else{
                showToast(it.exception.toString())
            }
        }
    }

    companion object {

    }
}