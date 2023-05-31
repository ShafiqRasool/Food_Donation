package com.riphah.food.ui.authntication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.riphah.food.R
import com.riphah.food.databinding.FragmentSignupBinding
import com.riphah.food.model.User
import com.riphah.food.showToast

class SignupFragment : Fragment() {
    private lateinit var binding:FragmentSignupBinding
    val firebaseAuth:FirebaseAuth=FirebaseAuth.getInstance()
    val firebaseUser:FirebaseUser?=firebaseAuth.currentUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_signup,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListeners(view)


    }

    private fun setUpListeners(view: View) {
        binding.textViewToLogin.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.Fragment_container_login,LoginFragment())
                addToBackStack(null)
            }
        }
        binding.buttonCreateAccount.setOnClickListener {
            val name:String=binding.UserNameEditText.text.toString().trim()
            val email:String=binding.UserEmailEditText.text.toString().trim()
            val password:String=binding.UserPasswordEditText.text.toString().trim()

            if(name.isNotEmpty()){

                binding.UserNameLayout.error=null

                if(email.isNotEmpty()){

                    binding.UserEmailLayout.error=null

                    if(password.isNotEmpty()){

                        binding.UserPasswordLayout.error=null

                        val user=User(name,email,password)
                        createUserAccount(user,view)

                    }else{
                        binding.UserPasswordLayout.error="PLease enter a password"
                    }
                }else{
                    binding.UserEmailLayout.error="Please enter email"
                }
            }else{
               binding.UserNameLayout.error="Please enter full name"

            }
        }
    }

    private fun createUserAccount(user: User,view: View) {
        firebaseAuth.createUserWithEmailAndPassword(user.email,user.password).addOnCompleteListener {
            if(it.isSuccessful) {
                Toast.makeText(view.context, "Account Created Successfully", Toast.LENGTH_SHORT)
                    .show()

                        } else {
                            Toast.makeText(
                                view.context,
                                "Something went wrong!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }



    companion object {

    }
    private fun sendEmailVerification(email:String) {
        firebaseUser?.let {
            it.sendEmailVerification().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("email sent to $email ")
                }
            }
        }
    }
}


