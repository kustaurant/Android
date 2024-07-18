package com.example.kustaurant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kustaurant.databinding.ActivityMainBinding
import com.example.kustaurant.presentation.ui.home.HomeFragment
import com.example.kustaurant.presentation.ui.tier.TierFragment


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.menu_random -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.menu_rank -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_frm, TierFragment()).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.menu_community -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_frm, CommunityFragment()).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.menu_mypage -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commit()
                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener true
                }
            }
        }
        binding.mainNavigation.selectedItemId = R.id.menu_home
    }
}