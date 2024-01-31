package com.showlive.assignment.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.showlive.assignment.R
import com.showlive.assignment.databinding.ActivityMainBinding
import com.showlive.assignment.ui.favorite.FavoriteFragment
import com.showlive.assignment.ui.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
    }

    private fun setupUi() = with(binding) {
        bottomNavigation.setOnItemSelectedListener {
            Log.e("BottomNavigation", "Selected item: ${it.itemId}")
            when (it.itemId) {
                R.id.search -> {
                    replaceFragment(SearchFragment.newInstance())
                    true
                }
                R.id.favorite -> {
                    replaceFragment(FavoriteFragment.newInstance())
                    true
                }
                else -> false
            }
        }

        bottomNavigation.selectedItemId = R.id.search
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    private fun loadData() {}

    private fun observeData() = with(viewModel) {

    }
}