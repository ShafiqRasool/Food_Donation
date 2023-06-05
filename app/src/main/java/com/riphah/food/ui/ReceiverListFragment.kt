package com.riphah.food.ui

import android.os.Bundle
import android.text.TextUtils.replace
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.riphah.food.R
import com.riphah.food.adapter.ReceiverAdapter
import com.riphah.food.databinding.FragmentReceiverListBinding
import com.riphah.food.model.ReceiverModel
import com.riphah.food.showToast
import com.riphah.food.utils.FirebaseUtils
import com.riphah.food.utils.FirebaseUtils.firebaseReceiverListReference

class ReceiverListFragment : Fragment(),ReceiverAdapter.CardClickListener {
    private lateinit var binding:FragmentReceiverListBinding
    lateinit var adapter:ReceiverAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentReceiverListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ReceiverAdapter(requireContext())
        adapter.setCardClickListener(this)
        binding.RecyclerViewReceiverList.adapter = adapter

        firebaseReceiverListReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val modelList = ArrayList<ReceiverModel>()
                for (singleReceiver in snapshot.children) {
                    val receiver = singleReceiver.getValue(ReceiverModel::class.java)
                    receiver?.let {
                        modelList.add(receiver)
                    }
                }
                adapter.updateData(modelList)
            }

            override fun onCancelled(error: DatabaseError) {
                showToast(error.toString())
            }

        })


    }


    companion object {


    }

    override fun onItemClick(model: ReceiverModel) {
        val bundle=Bundle()
        val donateFragment=DonateFragment()
        bundle.putParcelable("ReceiverModel",model)
        donateFragment.arguments=bundle
        parentFragmentManager.commit {
            replace(R.id.fragNavHost,donateFragment)
            addToBackStack(null)
        }
    }
}