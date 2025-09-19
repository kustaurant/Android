package com.kust.kustaurant.presentation.ui.mypage

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kust.kustaurant.databinding.ActivityMyPrivacyPolicyBinding
import com.kust.kustaurant.presentation.common.BaseActivity

class MyPrivacyPolicyActivity : BaseActivity() {
    lateinit var binding : ActivityMyPrivacyPolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPrivacyPolicyBinding.inflate(layoutInflater)

        initBack()
        initPrivacy()

        setContentView(binding.root)
    }

    private fun initPrivacy() {
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