package com.example.kustaurant.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.kustaurant.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 스플래시 화면 3초 후 온보딩 액티비티로 전환
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3초 동안 스플래시 화면을 보여줌
    }
}