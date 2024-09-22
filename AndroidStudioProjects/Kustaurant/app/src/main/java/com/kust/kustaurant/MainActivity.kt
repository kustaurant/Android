package com.kust.kustaurant

import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.kust.kustaurant.databinding.ActivityMainBinding
import com.kust.kustaurant.presentation.ui.community.CommunityBlankFragment
import com.kust.kustaurant.presentation.ui.community.CommunityFragment
import com.kust.kustaurant.presentation.ui.draw.DrawFragment
import com.kust.kustaurant.presentation.ui.home.HomeFragment
import com.kust.kustaurant.presentation.ui.mypage.MyPageFragment
import com.kust.kustaurant.presentation.ui.tier.TierFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var TIME_INTERVAL: Long = 2000
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this) {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.main_frm)
            if (currentFragment is HomeFragment) {
                if (backPressedTime + TIME_INTERVAL > System.currentTimeMillis()) {
                    finish()
                } else {
                    Toast.makeText(this@MainActivity, "뒤로가기를 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
                }
                backPressedTime = System.currentTimeMillis()
            } else {
                supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment(), "home").commit()
            }
        }

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
                R.id.menu_community -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_frm, CommunityBlankFragment()).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.menu_mypage -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_frm, MyPageFragment()).commit()
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