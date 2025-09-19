package com.kust.kustaurant.presentation.ui.mypage

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kust.kustaurant.databinding.ActivityMyNotificationBinding
import com.kust.kustaurant.presentation.common.BaseActivity

class MyNotificationActivity : BaseActivity() {
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