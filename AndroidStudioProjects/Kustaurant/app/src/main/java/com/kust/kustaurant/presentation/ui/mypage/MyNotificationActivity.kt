package com.kust.kustaurant.presentation.ui.mypage

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityMyNotificationBinding

class MyNotificationActivity : AppCompatActivity() {
    lateinit var binding : ActivityMyNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyNotificationBinding.inflate(layoutInflater)

        initBack()
        initNotification()

        setContentView(binding.root)
    }

    private fun initNotification() {
        binding.notiWvNotification.loadUrl("https://kustaurant.com/notice")
    }

    private fun initBack() {
        binding.notiBtnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}