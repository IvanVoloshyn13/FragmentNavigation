package com.example.fragmentnavigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fragmentnavigation.databinding.ActivityMainBinding
import com.example.fragmentnavigation.fragments.AboutFragment

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, AboutFragment())
            .commit()
    }
}