package com.example.fragmentnavigation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fragmentnavigation.R
import com.example.fragmentnavigation.contract.HasCustomTitle
import com.example.fragmentnavigation.contract.Options
import com.example.fragmentnavigation.contract.navigator
import com.example.fragmentnavigation.databinding.FragmentBoxSelectionBinding
import com.example.fragmentnavigation.databinding.ItemBoxBinding
import java.sql.Time
import kotlin.math.max
import kotlin.properties.Delegates
import kotlin.random.Random

class BoxSelectionFragment : Fragment(), HasCustomTitle {

    private lateinit var options: Options
    private var timerStartTimestamp by Delegates.notNull<Long>()
    private var boxIndex by Delegates.notNull<Int>()
    private var alreadyDone = false
    private var timerHandler: TimeHandler? = null
    private val binding by lazy {
        FragmentBoxSelectionBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options = arguments?.getParcelable(ARG_OPTIONS)
            ?: throw IllegalArgumentException("Can't launch BoxSelectionActivity without options")

        boxIndex = savedInstanceState?.getInt(KEY_INDEX) ?: Random.nextInt(options.boxCount)

        timerHandler = if (options.isTimerEnabled) {
            TimeHandler()
        } else {
            null
        }
        TimeHandler().onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createBoxes()
        return binding.root
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, boxIndex)
        timerHandler?.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        timerHandler?.onStart()
    }

    override fun onStop() {
        super.onStop()
        timerHandler?.onStop()
    }

    override fun getTitleRes(): Int = R.string.select_box

    private fun createBoxes() {
        val boxBinding = (0 until options.boxCount).map { index ->
            val boxBinding = ItemBoxBinding.inflate(layoutInflater)
            boxBinding.root.id = View.generateViewId()
            boxBinding.boxTitleTextView.text = getString(R.string.box_title, index + 1)
            boxBinding.root.setOnClickListener { view -> onBoxSelected(view) }
            boxBinding.root.tag = index
            binding.root.addView(boxBinding.root)
            boxBinding
        }
        binding.flow.referencedIds = boxBinding.map { it.root.id }.toIntArray()
    }

    private fun onBoxSelected(view: View) {
        if (view.tag as Int == boxIndex) {
            alreadyDone = true // disabling timer if the user made a right choice
            navigator().showCongratulationsScreen()
        } else {
            Toast.makeText(context, R.string.empty_box, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTimerUi() {
        if (getRemainingSeconds() >= 0) {
            binding.timerTextView.visibility = View.VISIBLE
            binding.timerTextView.text = getString(R.string.timer_value, getRemainingSeconds())
        } else {
            binding.timerTextView.visibility = View.GONE
        }
    }

    fun showTimerEndDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.the_end))
            .setMessage(getString(R.string.timer_end_message))
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok) { _, _ -> navigator().goBack() }
            .create()
        dialog.show()
    }

    private fun getRemainingSeconds(): Long {
        val finishedAt = timerStartTimestamp + TIMER_DURATION
        return max(0, (finishedAt - System.currentTimeMillis()) / 1000)
    }

    inner class TimeHandler {
        private var timer: CountDownTimer? = null

        fun onCreate(savedInstanceState: Bundle?) {
            timerStartTimestamp =
                savedInstanceState?.getLong(KEY_START_TIMESTAMP) ?: System.currentTimeMillis()
            alreadyDone = savedInstanceState?.getBoolean(KEY_ALREADY_DONE) ?: false
        }

        fun onSaveInstanceState(outState: Bundle) {
            outState.putLong(KEY_START_TIMESTAMP, timerStartTimestamp)
            outState.putBoolean(KEY_ALREADY_DONE, alreadyDone)
        }

        fun onStart() {
            if (alreadyDone) return
            // timer is paused when app is minimized (onTick is not called), but we remember the initial
            // timestamp when the screen has been launched, so actually the dialog is displayed in 10
            // seconds after the initial timestamp anyway. If the app is minimized for more than 10 seconds
            // the dialog is shown immediately after restoring the app.

            timer = object : CountDownTimer(getRemainingSeconds() * 1000, 1000) {
                override fun onTick(millisUntilFinish: Long) {
                    updateTimerUi()
                }

                override fun onFinish() {
                    updateTimerUi()
                    showTimerEndDialog()
                }


            }
            updateTimerUi()
            timer?.start()
        }

        fun onStop() {
            timer?.cancel()
            timer = null
        }

    }

    companion object {
        @JvmStatic
        private val ARG_OPTIONS = "EXTRA_OPTIONS"

        @JvmStatic
        private val KEY_INDEX = "KEY_INDEX"

        @JvmStatic
        private val KEY_START_TIMESTAMP = "KEY_START_TIMESTAMP"

        @JvmStatic
        private val KEY_ALREADY_DONE = "KEY_ALREADY_DONE"

        @JvmStatic
        private val TIMER_DURATION = 10_000L

        fun newInstance(options: Options): BoxSelectionFragment {
            val args = Bundle()
            args.putParcelable(ARG_OPTIONS, options)
            val fragment = BoxSelectionFragment()
            fragment.arguments = args
            return fragment
        }
    }
}