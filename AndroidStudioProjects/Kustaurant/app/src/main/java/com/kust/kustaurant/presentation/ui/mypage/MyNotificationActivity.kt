package com.kust.kustaurant.presentation.ui.mypage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kust.kustaurant.databinding.ActivityMyNotificationBinding

class MyNotificationActivity : AppCompatActivity() {
    lateinit var binding : ActivityMyNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyNotificationBinding.inflate(layoutInflater)

        initBack()

        setContentView(binding.root)
    }

    private fun initBack() {
        binding.notiBtnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}