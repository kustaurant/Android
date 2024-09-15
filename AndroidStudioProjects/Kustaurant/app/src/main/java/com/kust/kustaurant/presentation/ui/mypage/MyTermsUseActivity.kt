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
import com.kust.kustaurant.databinding.ActivityMyTermsUseBinding

class MyTermsUseActivity : AppCompatActivity() {
    lateinit var binding : ActivityMyTermsUseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyTermsUseBinding.inflate(layoutInflater)

        initBack()
        initTermsUse()

        setContentView(binding.root)
    }

    private fun initTermsUse() {
        binding.termsUseWvTerms.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.termsUseLaLoading.visibility = View.VISIBLE
                binding.termsUseWvTerms.visibility = View.GONE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.termsUseLaLoading.visibility = View.GONE
                binding.termsUseWvTerms.visibility = View.VISIBLE
            }
        }
        binding.termsUseWvTerms.loadUrl("https://kustaurant.com/terms_of_use")
    }


    private fun initBack() {
        binding.myFlBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}