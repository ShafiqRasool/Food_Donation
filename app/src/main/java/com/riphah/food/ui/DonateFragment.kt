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
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.riphah.food.R
import com.riphah.food.databinding.FragmentDonateBinding
import com.riphah.food.model.DonnerModel
import com.riphah.food.model.ReceiverModel
import com.riphah.food.showToast
import com.riphah.food.utils.FirebaseUtils
import com.riphah.food.utils.FirebaseUtils.firebaseDonnerListReference
import java.util.UUID


class DonateFragment : Fragment() {

    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private lateinit var binding: FragmentDonateBinding
    private lateinit var imageFileUri: Uri;
    private var imageSelected:Boolean=false;
    private var noOfPeople: String=""

    private var wantTransport: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDonateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiverModel = arguments?.getParcelable<ReceiverModel>("receiverModel")
        setUpViews(view)
        setUpListeners()


    }

    private fun setUpListeners() {

        ///get values from auto complete text
        binding.noOfPeople.setOnItemClickListener { parent, view, position, rowId ->
            noOfPeople = parent.getItemAtPosition(position) as String
        }
        binding.ButtonSubmit.setOnClickListener {
            val name: String = binding.DonnerName.text.toString().trim()
            val phone: String = binding.DonnerPhone.text.toString().trim()
            val address: String = binding.DonnerAddress.text.toString().trim()
            val foodType: String = binding.FoodType.text.toString().trim()
            binding.ProgressbarDonner.visibility=View.VISIBLE
            if(validateInputFields()) {

                val filename = UUID.randomUUID().toString()
                //image reference
                val storageReference = storage.reference.child("Images").child("$filename")
                if (imageSelected) {
                    //putting image file to firebase
                    storageReference.putFile(imageFileUri).addOnSuccessListener {
                        //getting download url of file
                        storageReference.downloadUrl.addOnSuccessListener {
                            //now it is ready to upload
                            val donner=DonnerModel(name,address,phone,foodType,noOfPeople,it.toString(),wantTransport.toString())
                            //upload data to FB Database
                            val token= firebaseDonnerListReference.push().key
                            firebaseDonnerListReference.child(token.toString()).setValue(donner).addOnSuccessListener {
                                binding.ProgressbarDonner.visibility=View.GONE
                                clearTextandImage()
                                showToast("Your Donation Information is added Successfully!")


                            }.addOnFailureListener {
                                binding.ProgressbarDonner.visibility=View.GONE
                                showToast(it.message.toString())
                            }
                        }.addOnFailureListener {
                            binding.ProgressbarDonner.visibility=View.GONE
                            showToast("error download url"+it.toString())
                        }
                    }.addOnFailureListener {
                        binding.ProgressbarDonner.visibility=View.GONE
                        showToast("Image is not uploaded"+it.toString())
                    }
                }
            }



        }
    }

    private fun clearTextandImage() {
            binding.DonnerName.text=null
            binding.DonnerPhone.text=null
            binding.DonnerAddress.text=null
            binding.FoodType.text=null
            binding.ImagePreview.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.baseline_add_a_photo_24))


    }

    private fun validateInputFields(): Boolean {
        val name: String = binding.DonnerName.text.toString().trim()
        val phone: String = binding.DonnerPhone.text.toString().trim()
        val address: String = binding.DonnerAddress.text.toString().trim()
        val foodType: String = binding.FoodType.text.toString().trim()
        var noOfPeople: String = ""

        binding.noOfPeople.setOnItemClickListener { parent, view, position, rowId ->
            noOfPeople = parent.getItemAtPosition(position) as String
        }

        var isValid = true

        if (name.isEmpty()) {
            binding.DonnerName.error = "Name cannot be empty"
            isValid = false
        } else {
            binding.DonnerName.error = null
        }

        if (phone.isEmpty()) {
            binding.DonnerPhone.error = "Phone cannot be empty"
            isValid = false
        } else {
            binding.DonnerPhone.error = null
        }

        if (address.isEmpty()) {
            binding.DonnerAddress.error = "Address cannot be empty"
            isValid = false
        } else {
            binding.DonnerAddress.error = null
        }

        if (foodType.isEmpty()) {
            binding.FoodType.error = "Food type cannot be empty"
            isValid = false
        } else {
            binding.FoodType.error = null
        }



        return isValid
    }



    private fun setUpViews(view: View) {
        val noOfPeoples =
            listOf<String>("5 - 10", "10 - 30", "30 - 80", "80 - 100", "More then 100")
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, noOfPeoples)
        binding.noOfPeople.setAdapter(adapter)
        binding.cardViewImagePreview.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
            binding.ImagePreview.isClickable = false
        }
        binding.checkBoxYes.setOnClickListener() {
            if (binding.checkBoxNo.isChecked)
                binding.checkBoxNo.isChecked = false
            wantTransport = true
        }
        binding.checkBoxNo.setOnClickListener() {
            if (binding.checkBoxYes.isChecked)
                binding.checkBoxYes.isChecked = false
            wantTransport = false
        }

    }


    companion object {

    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            binding.ImagePreview.isClickable = true
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                imageFileUri = data?.data!!
                binding.ImagePreview.setImageURI(imageFileUri)
                imageSelected=true
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                // Toast.makeText(, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                //  Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
}


