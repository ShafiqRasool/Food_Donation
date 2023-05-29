package com.riphah.food.ui

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.riphah.food.R
import com.riphah.food.databinding.FragmentDonateBinding


class DonateFragment : Fragment() {


    private lateinit var binding: FragmentDonateBinding
    private lateinit var imageFileUri:Uri;
    //firebase
    private lateinit var storageReference:StorageReference
    private lateinit var databaseReference:DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_donate,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews(view)


    }

    private fun setUpViews(view: View) {
        val noOfPeoples = listOf<String>("5 - 10","10 - 30","30 - 80","80 - 100","More then 100")
        val adapter=ArrayAdapter(view.context,android.R.layout.simple_spinner_dropdown_item,noOfPeoples)
        binding.noOfPeople.setAdapter(adapter)
        binding.ImagePreview.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
            binding.ImagePreview.isClickable=false
        }
        binding.checkBoxYes.setOnClickListener(){
            if(binding.checkBoxNo.isChecked)
                binding.checkBoxNo.isChecked=false
        }
        binding.checkBoxNo.setOnClickListener(){
            if(binding.checkBoxYes.isChecked)
                binding.checkBoxYes.isChecked=false
        }

    }


    companion object {

    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            binding.ImagePreview.isClickable=true
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                imageFileUri = data?.data!!

                binding.ImagePreview.setImageURI(imageFileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
               // Toast.makeText(, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
              //  Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
}


