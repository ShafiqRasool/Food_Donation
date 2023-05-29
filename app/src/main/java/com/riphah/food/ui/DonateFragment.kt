package com.riphah.food.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.navigation.fragment.findNavController
import com.riphah.food.R


class DonateFragment : Fragment() {

    lateinit var spinnerNoOfPeoples:AutoCompleteTextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_donate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinnerNoOfPeoples=view.findViewById(R.id.noOfPeople)

        val noOfPeoples = listOf<String>("5 - 10","10 - 30","30 - 80","80 - 100","More then 100")
        val adapter=ArrayAdapter(view.context,android.R.layout.simple_spinner_dropdown_item,noOfPeoples)
        spinnerNoOfPeoples.setAdapter(adapter)






    }


    companion object {

    }
}