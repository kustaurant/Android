package com.kust.kustaurant.presentation.ui.mypage

import android.os.Bundle
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
        binding.privacyWvNotification.loadUrl("https://kustaurant.com/privacy-policy")
    }

    private fun initBack() {
        binding.myFlBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}