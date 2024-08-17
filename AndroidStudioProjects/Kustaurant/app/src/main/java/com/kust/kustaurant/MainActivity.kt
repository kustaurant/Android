package com.kust.kustaurant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kust.kustaurant.databinding.ActivityMainBinding
import com.kust.kustaurant.presentation.ui.draw.DrawFragment
import com.kust.kustaurant.presentation.ui.home.HomeFragment
import com.kust.kustaurant.presentation.ui.mypage.MyPageFragment
import com.kust.kustaurant.presentation.ui.tier.TierFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
                    supportFragmentManager.beginTransaction().replace(R.id.main_frm, DrawFragment()).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.menu_rank -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_frm, TierFragment()).commit()
                    return@setOnItemSelectedListener true
                }
//                R.id.menu_community -> {
//                    supportFragmentManager.beginTransaction().replace(R.id.main_frm, CommunityFragment()).commit()
//                    return@setOnItemSelectedListener true
//                }
//                R.id.menu_mypage -> {
//                    supportFragmentManager.beginTransaction().replace(R.id.main_frm, MyPageFragment()).commit()
//                    return@setOnItemSelectedListener true
//                }
                else -> {
                    return@setOnItemSelectedListener true
                }
            }
        }
        binding.mainNavigation.selectedItemId = R.id.menu_home
    }
}