package com.kust.kustaurant.presentation.ui.mypage

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
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
        binding.notiWvNotification.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.notiLaLoading.visibility = View.VISIBLE
                binding.notiWvNotification.visibility = View.GONE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.notiLaLoading.visibility = View.GONE
                binding.notiWvNotification.visibility = View.VISIBLE
            }
        }
        binding.notiWvNotification.loadUrl("https://kustaurant.com/notice")
    }

    private fun initBack() {
        binding.myFlBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}