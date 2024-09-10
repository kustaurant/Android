package com.kust.kustaurant.presentation.ui.mypage

import android.os.Bundle
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
        binding.termsUseWvTerms.loadUrl("https://kustaurant.com/terms_of_use")
    }

    private fun initBack() {
        binding.myFlBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}