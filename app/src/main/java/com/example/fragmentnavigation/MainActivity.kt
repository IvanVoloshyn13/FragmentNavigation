package com.example.fragmentnavigation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.window.OnBackInvokedCallback
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.BuildCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.example.fragmentnavigation.contract.Navigator
import com.example.fragmentnavigation.contract.Options
import com.example.fragmentnavigation.contract.ResultListener
import com.example.fragmentnavigation.databinding.ActivityMainBinding
import com.example.fragmentnavigation.fragments.AboutFragment
import com.example.fragmentnavigation.fragments.BoxSelectionFragment
import com.example.fragmentnavigation.fragments.MenuFragment
import com.example.fragmentnavigation.fragments.OptionsFragment

class MainActivity : AppCompatActivity(), Navigator {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val currentFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateUi()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, MenuFragment())
                .commit()
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    override fun showBoxSelectionScreen(options: Options) {
        launchFragment(BoxSelectionFragment())
    }

    override fun showOptionsScreen(options: Options) {
        launchFragment(OptionsFragment.newInstance(options))
    }

    override fun showAboutScreen() {
        launchFragment(AboutFragment())
    }

    override fun showCongratulationsScreen() {
        TODO("Not yet implemented")
    }

    override fun goBack() {
        onBackPressedDispatcher.onBackPressed()
//        onBackPressedDispatcher.addCallback(this, @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//        object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                onBackPressedDispatcher.onBackPressed()
//            }
//
//        })
    }

    override fun goToMenu() {
        launchFragment(MenuFragment())
    }

    override fun <T : Parcelable> publishResult(result: T) {
        TODO("Not yet implemented")
    }

    override fun <T : Parcelable> listenResult(
        clazz: Class<T>,
        owner: LifecycleOwner,
        listener: ResultListener<T>
    ) {
        TODO("Not yet implemented")
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun updateUi() {

    }
}