package com.example.fragmentnavigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fragmentnavigation.contract.Options
import com.example.fragmentnavigation.contract.navigator
import com.example.fragmentnavigation.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private lateinit var options: Options

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options = savedInstanceState?.getParcelable(KEY_OPTIONS) ?: Options.DEFAULT
    }

    private val binding by lazy { FragmentMenuBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.aboutButton.setOnClickListener {
            navigator().showAboutScreen()
        }
        binding.optionsButton.setOnClickListener {
            navigator().showOptionsScreen(options = Options(4, true))
        }
    }

    companion object {
        @JvmStatic private val KEY_OPTIONS = "OPTIONS"
    }
}