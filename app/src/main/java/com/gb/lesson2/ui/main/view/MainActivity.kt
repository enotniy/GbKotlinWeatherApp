package com.gb.lesson2.ui.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gb.lesson2.R
import com.gb.lesson2.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private val binding: MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}