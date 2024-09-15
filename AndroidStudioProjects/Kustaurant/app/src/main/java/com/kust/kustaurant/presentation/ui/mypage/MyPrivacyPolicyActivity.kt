package com.kust.kustaurant.presentation.ui.mypage

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kust.kustaurant.R
import com.kust.kustaurant.databinding.ActivityMyPrivacyPolicyBinding

class MyPrivacyPolicyActivity : AppCompatActivity() {
    lateinit var binding : ActivityMyPrivacyPolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPrivacyPolicyBinding.inflate(layoutInflater)

        initBack()
        initNotification()

        setContentView(binding.root)
    }

    private fun initNotification() {
        binding.privacyWvNotification.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.privacyLaLoading.visibility = View.VISIBLE
                binding.privacyWvNotification.visibility = View.GONE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.privacyLaLoading.visibility = View.GONE
                binding.privacyWvNotification.visibility = View.VISIBLE
            }
        }
        binding.privacyWvNotification.loadUrl("https://kustaurant.com/privacy-policy")
    }

    private fun initBack() {
        binding.myFlBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}