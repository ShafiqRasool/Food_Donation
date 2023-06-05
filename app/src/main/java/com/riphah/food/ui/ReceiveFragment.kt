package com.riphah.food.ui

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.riphah.food.R
import com.riphah.food.databinding.FragmentReceiveBinding
import com.riphah.food.model.ReceiverModel
import com.riphah.food.showToast
import com.riphah.food.utils.FirebaseUtils.firebaseAuth
import java.util.UUID


class ReceiveFragment : Fragment() {

    private lateinit var binding: FragmentReceiveBinding
    private lateinit var occupationText: String
    private lateinit var receiverModel: ReceiverModel
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private lateinit var imageFileUri: Uri
    private var firebaseDatabase =
        FirebaseDatabase.getInstance().getReference("Users").child("Receivers")
    private var imageSelected: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReceiveBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intiViews(view)
    }

    private fun intiViews(view: View) {

        //ImageView functionality
        binding.cardViewImagePreviewReceiver.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    binding.ImagePreviewReceiver.isClickable = false
                    startForProfileImageResult.launch(intent)
                }
        }
        //Radio button functionality
        binding.RadioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.RadioIndividual) {
                binding.receiverCompanyLayout.isVisible = false
                occupationText = "Individual"
            } else if (checkedId == R.id.RadioOrganization) {
                binding.receiverCompanyLayout.isVisible = true

            }
        }
        binding.FloatingActionButtonDonate.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            if (validateInputs()) {
                //inputs are now valid so add them to firebase
                val progressBar: ProgressBar = ProgressBar(requireContext())
                progressBar.isVisible = true
                //upload Image to firebase
                val userID: String = firebaseAuth.currentUser.toString()
                val filename = UUID.randomUUID().toString()
                //image reference
                val storageReference = storage.reference.child("Images").child("$filename")
                if (imageSelected) {
                    //putting image file to firebase
                    storageReference.putFile(imageFileUri).addOnSuccessListener {
                        //now getting the file url
                        storageReference.downloadUrl.addOnSuccessListener {
                            //updating model with image url
                            receiverModel.imageUrl = it.toString()

                            //uploading other data to realtime db
                            val token = firebaseDatabase.push().key
                            firebaseDatabase.child(token.toString()).setValue(receiverModel)
                                .addOnSuccessListener {
                                    binding.progressBar.visibility = View.GONE
                                    removeText()
                                    showToast("Submitted successful")

                                }.addOnFailureListener {
                                    showToast("Error " + it.message.toString())
                                }
                        }.addOnFailureListener {
                            binding.progressBar.visibility = View.GONE
                            showToast("Error storage" + it.message.toString())
                        }
                    }
                } else {
                    showToast("Please select Picture!")
                }

            }
        }
    }

    private fun removeText() {
        binding.receiverName.text = null
        binding.receiverPhone.text = null
        binding.receiverAddres.text = null
        binding.receiverCompanyName.text = null
        binding.ImagePreviewReceiver.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.baseline_add_a_photo_24))
    }

    private fun validateInputs(): Boolean {
        val name = binding.receiverName.text.toString().trim()
        val phone = binding.receiverPhone.text.toString().trim()
        val address = binding.receiverAddres.text.toString().trim()
        var occupationText = "Individual"

        if (binding.receiverCompanyLayout.isVisible) {
            occupationText = binding.receiverCompanyName.text.toString().trim()
        }

        if (name.isEmpty()) {
            binding.receiverName.error = "Name cannot be empty"
            return false
        }

        if (phone.isEmpty()) {
            binding.receiverPhone.error = "Phone cannot be empty"
            return false
        }

        if (address.isEmpty()) {
            binding.receiverAddres.error = "Address cannot be empty"
            return false
        }

        if (binding.receiverCompanyLayout.isVisible && binding.receiverCompanyName.text!!.isEmpty()) {
            binding.receiverCompanyLayout.error = "Occupation cannot be empty"
            return false
        }

        // All values are not empty, clear any previous errors
        binding.receiverNameLayout.error = null
        binding.receiverPhoneLayout.error = null
        binding.receiverAddressLayout.error = null
        binding.receiverCompanyLayout.error = null

        // Create the ReceiverModel object or perform further operations
        receiverModel = ReceiverModel(name, phone, address, occupationText, null)

        return true
    }


    companion object {
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            binding.ImagePreviewReceiver.isClickable = true
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                imageFileUri = data?.data!!
                binding.ImagePreviewReceiver.setImageURI(imageFileUri)
                imageSelected = true
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showToast(ImagePicker.getError(data))
            } else {
                imageSelected = false
                showToast("Task Cancelled")
            }
        }
}