package com.example.fragmentnavigation.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.os.BuildCompat
import androidx.fragment.app.Fragment
import com.example.fragmentnavigation.R
import com.example.fragmentnavigation.contract.CustomAction
import com.example.fragmentnavigation.contract.HasCustomAction
import com.example.fragmentnavigation.contract.HasCustomTitle
import com.example.fragmentnavigation.contract.Options
import com.example.fragmentnavigation.contract.navigator
import com.example.fragmentnavigation.databinding.FragmentOptionsBinding

class OptionsFragment : Fragment(), HasCustomTitle, HasCustomAction {
    private val binding by lazy { FragmentOptionsBinding.inflate(layoutInflater) }
    private lateinit var options: Options
    private lateinit var boxCountItem: List<BoxCountItem>
    private lateinit var adapter: ArrayAdapter<BoxCountItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            savedInstanceState?.getParcelable(KEY_OPTIONS) ?: arguments?.getParcelable(
                ARG_OPTIONS
            )
            ?: throw IllegalArgumentException("You need to specify options to launch this fragment")
        } else {
            savedInstanceState?.getParcelable(KEY_OPTIONS, Options::class.java)
                ?: arguments?.getParcelable(
                    ARG_OPTIONS, Options::class.java
                )
                ?: throw IllegalArgumentException("You need to specify options to launch this fragment")
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupSpinner()
        setupCheckBox()
        updateUi()
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancelButton.setOnClickListener { navigator().goBack()}
        binding.confirmButton.setOnClickListener { onConfirmPressed() }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
    }

    private fun setupSpinner() {
        boxCountItem =
            (1..6).map { BoxCountItem(it, resources.getQuantityString(R.plurals.boxes, it, it)) }
        adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            boxCountItem
        )
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1)
        binding.boxCountSpinner.adapter = adapter
        binding.boxCountSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val count = boxCountItem[position].count
                    options = options.copy(boxCount = count)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
    }

    private fun setupCheckBox() {
        binding.enableTimerCheckBox.setOnClickListener {
            options = options.copy(isTimerEnabled = binding.enableTimerCheckBox.isChecked)
        }
    }

    private fun updateUi() {
        binding.enableTimerCheckBox.isChecked = options.isTimerEnabled
        val currentIndex = boxCountItem.indexOfFirst { it.count == options.boxCount }
        binding.boxCountSpinner.setSelection(currentIndex)
    }


    override fun getCustomAction(): CustomAction {
        return CustomAction(
            iconRes = R.drawable.ic_done,
            textRes = R.string.done,
            onCustomAction = Runnable {
                onConfirmPressed()
            }
        )
    }

    override fun getTitleRes(): Int {
        return R.string.options
    }

    private fun onConfirmPressed() {
        navigator().publishResult(options)
        navigator().goBack()
    }

    companion object {
        @JvmStatic
        private val ARG_OPTIONS = "ARG_OPTIONS"

        @JvmStatic
        private val KEY_OPTIONS = "KEY_OPTIONS"

        @JvmStatic
        fun newInstance(options: Options): OptionsFragment {
            val args = Bundle()
            args.putParcelable(ARG_OPTIONS, options)
            val fragment = OptionsFragment()
            fragment.arguments = args
            return fragment
        }
    }


    class BoxCountItem(
        val count: Int,
        private val optionTitle: String
    ) {
        override fun toString(): String {
            return optionTitle
        }
    }
}