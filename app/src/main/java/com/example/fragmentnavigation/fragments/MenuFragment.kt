package com.example.fragmentnavigation.fragments

import android.os.Build
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
        options = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                savedInstanceState?.getParcelable(KEY_OPTIONS) ?: Options.DEFAULT
                ?: throw IllegalArgumentException("You need to specify options to launch this fragment")
            } else {
                savedInstanceState?.getParcelable(KEY_OPTIONS, Options::class.java)
                    ?: arguments?.getParcelable(
                        KEY_OPTIONS, Options::class.java
                    )
                    ?: throw IllegalArgumentException("You need to specify options to launch this fragment")
            }
    }

    private val binding by lazy { FragmentMenuBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        navigator().listenResult(Options::class.java, viewLifecycleOwner) {
            this.options = it
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.aboutButton.setOnClickListener {
            navigator().showAboutScreen()
        }
        binding.optionsButton.setOnClickListener {
            navigator().showOptionsScreen(options = options)
        }
    }

    companion object {
        @JvmStatic private val KEY_OPTIONS = "OPTIONS"
    }
}