package com.riphah.food.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

object FirebaseUtils {
    val firebaseAuth:FirebaseAuth=FirebaseAuth.getInstance()
    val firebaseUser:FirebaseUser? = firebaseAuth.currentUser
    val firebaseReceiverListReference=FirebaseDatabase.getInstance().getReference("Users").child("Receivers")
    val firebaseDonnerListReference=FirebaseDatabase.getInstance().getReference("Users").child("Donners")

}