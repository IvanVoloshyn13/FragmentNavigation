package com.example.fragmentnavigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fragmentnavigation.R
import com.example.fragmentnavigation.contract.navigator
import com.example.fragmentnavigation.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private val binding by lazy { FragmentAboutBinding.inflate(layoutInflater) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bttOk.setOnClickListener {
            navigator().goBack()
        }
    }
}