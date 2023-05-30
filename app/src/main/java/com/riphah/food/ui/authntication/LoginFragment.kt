package com.riphah.food.ui.authntication

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

class LoginFragment : Fragment() {

    lateinit var binding:FragmentLoginBinding
    private val firebaseAuth:FirebaseAuth= FirebaseAuth.getInstance()
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
           val email= binding.UserEmailEditTextSignIn.text.toString().trim()
            val password=binding.UserPasswordEditTextSignIn.text.toString().trim()
            if(email.isNotEmpty()){
                binding.UserEmailLayoutSignIn.error=null
                if(password.isNotEmpty()){
                    binding.UserPasswordLayoutSignIn.error=null
                    val user= User(null,email,password)
                    loginUser(user)
                }else{
                    binding.UserPasswordLayoutSignIn.error="Please enter email"

                }
            }else{
                binding.UserEmailLayoutSignIn.error="Please Enter Password"

            }
        }
    }

    private fun loginUser(user:User){

        firebaseAuth.signInWithEmailAndPassword(user.email,user.password).addOnSuccessListener {
               // showToast("Login successful")
               showToast(""+firebaseAuth.currentUser.toString())
        }.addOnFailureListener{
            showToast("Failure"+it.message.toString())

        }
    }

    companion object {

    }
}